import CurrencyViewModelTest.Companion.dataFromDB
import CurrencyViewModelTest.Companion.requestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.Clock
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import pro.tanay.currency_conversion.data.RepositoryImpl
import pro.tanay.currency_conversion.data.local.LocalRepositoryImpl
import pro.tanay.currency_conversion.domain.IDatabaseRepository
import pro.tanay.currency_conversion.domain.ILocalRepository
import pro.tanay.currency_conversion.domain.IPreferenceRepository
import pro.tanay.currency_conversion.domain.IRemoteRepository
import pro.tanay.currency_conversion.domain.IRepository
import pro.tanay.currency_conversion.domain.model.ApiResponse
import pro.tanay.currency_conversion.domain.model.Currency
import pro.tanay.currency_conversion.domain.model.RequestState
import pro.tanay.currency_conversion.ext.CoroutineDispatcherProvider
import pro.tanay.currency_conversion.viewmodel.CurrencyViewModel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyViewModelTest : KoinTest {

    private lateinit var viewModel: CurrencyViewModel

    private fun testModule(coroutineDispatcherProvider: CoroutineDispatcherProvider): Module {
        val module = module {
            single<IPreferenceRepository> { FakePreferenceRepo() }
            single<IDatabaseRepository> { FakeDatabaseRepo() }

            single<ILocalRepository> { LocalRepositoryImpl(get(), get()) }
            single<IRemoteRepository> { FakeCurrencyRepo() }

            single<IRepository> { RepositoryImpl(get(), get()) }

            single { CurrencyViewModel(get(), coroutineDispatcherProvider) }
        }
        return module
    }


    @BeforeTest
    fun setUp() {
        val dispatcher = StandardTestDispatcher()
        val coroutineDispatcherProvider = CoroutineDispatcherProvider(
            main = dispatcher,
            default = dispatcher,
            io = dispatcher
        )

        startKoin {
            modules(testModule(coroutineDispatcherProvider))
        }

        Dispatchers.setMain(dispatcher)
        viewModel = get()
        addDataForApi()
        addDataToDatabase()
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }


    @Test
    fun `test preference timestamp data is correct`() = runTest {
        val timestamp = 1732423603L

        viewModel.repository.localRepository().preference().saveTimestamp(timestamp)

        val result = viewModel.repository.localRepository().preference().getTimestamp().first()

        assertEquals(result, timestamp)
    }

    @Test
    fun `test when 30 minutes passed then data should be fetched from API`() = runTest {
        val oldTimestamp = 1732423603L

        viewModel.repository.localRepository().preference().saveTimestamp(oldTimestamp)

        val result = viewModel.repository.localRepository().preference().getTimestamp().first()

        assertEquals(oldTimestamp, result)

        viewModel.initialization()

        // at first stage will be Loading
        assertTrue(viewModel.state.first() is RequestState.Loading)

        runCurrent()

        val dataFromApi = viewModel.getCurrencyList()
        assertEquals(dataFromApi, apiDataList())

        // at last stage will be Success
        assertTrue(viewModel.state.first() is RequestState.Success)
    }

    @Test
    fun `test when 30 minutes not passed then data should be fetched from DB`() = runTest {
        val oldTimestamp = Clock.System.now().epochSeconds

        viewModel.repository.localRepository().preference().saveTimestamp(oldTimestamp)

        val result = viewModel.repository.localRepository().preference().getTimestamp().first()

        assertEquals(oldTimestamp, result)

        viewModel.initialization()

        // at first stage will be Loading
        assertTrue(viewModel.state.first() is RequestState.Loading)

        runCurrent()

        val dataFromDB = viewModel.getCurrencyList()
        assertEquals(dataFromDB, dbList())

        // at last stage will be Success
        assertTrue(viewModel.state.first() is RequestState.Success)
    }

    @Test
    fun `test retry when data is not available in db`() = runTest {
        val oldTimestamp = -1L

        viewModel.repository.localRepository().preference().saveTimestamp(oldTimestamp)

        val result = viewModel.repository.localRepository().preference().getTimestamp().first()

        assertEquals(oldTimestamp, result)

        viewModel.retry()

        // at first stage will be Loading
        assertTrue(viewModel.state.first() is RequestState.Loading)

        runCurrent()

        val dataFromApi = viewModel.getCurrencyList()
        assertEquals(dataFromApi, apiDataList())

        // at first stage will be Success
        assertTrue(viewModel.state.first() is RequestState.Success)
    }

    @Test
    fun `test process with USD as base currency with 10 as Input`() = runTest {
        val oldTimestamp = -1L

        viewModel.repository.localRepository().preference().saveTimestamp(oldTimestamp)

        val result = viewModel.repository.localRepository().preference().getTimestamp().first()

        assertEquals(oldTimestamp, result)

        viewModel.initialization()

        runCurrent()

        viewModel.process("10", Currency("USD", 1.0))

        runCurrent()

        val dataFromApi = viewModel.getCurrencyList()
        assertEquals(dataFromApi, apiDataList())

        // at first stage will be Success
        assertTrue(viewModel.state.first() is RequestState.Success)

        val data = viewModel.state.first() as RequestState.Success

        assertEquals(data.data.currencyList[0].value, apiDataList()[0].value * 10)
        assertEquals(data.data.currencyList[1].value, apiDataList()[1].value * 10)
    }

    private fun addDataForApi(): RequestState {
        requestState = RequestState.Success(ApiResponse(apiDataList()))
        return requestState
    }

    private fun addDataToDatabase() {
        dataFromDB = dbList()
    }

    private fun dbList(): List<Currency> {
        val aed = Currency("AED", 3.673)
        val azn = Currency("AZN", 1.7)
        return listOf(aed, azn)
    }

    private fun apiDataList(): List<Currency> {
        val aed = Currency("CNH", 7.2595)
        val azn = Currency("ERN", 15.0)
        return listOf(aed, azn)
    }

    companion object {
        var requestState: RequestState = RequestState.Loading
        var dataFromDB = listOf<Currency>()
    }
}

class FakeCurrencyRepo : IRemoteRepository {

    override suspend fun getLatestExchangeRates(): RequestState {
        return requestState
    }
}

class FakePreferenceRepo : IPreferenceRepository {

    private var ts = 0L

    override suspend fun saveTimestamp(timestamp: Long) {
        ts = timestamp
    }

    override fun getTimestamp(): Flow<Long> {
        return flow { emit(ts) }
    }

}

class FakeDatabaseRepo : IDatabaseRepository {

    override fun getAllCurrencies(): Flow<List<Currency>> {
        return flow { emit(dataFromDB) }
    }

    override suspend fun insertAll(list: MutableList<Currency>) {
        dataFromDB = list
    }

}