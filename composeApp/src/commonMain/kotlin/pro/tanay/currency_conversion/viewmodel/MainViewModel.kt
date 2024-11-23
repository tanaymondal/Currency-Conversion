package pro.tanay.currency_conversion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import pro.tanay.currency_conversion.database.CurrencyDatabase
import pro.tanay.currency_conversion.domain.ICurrencyApiRepository
import pro.tanay.currency_conversion.domain.IPreferenceRepository
import pro.tanay.currency_conversion.domain.model.ApiResponse
import pro.tanay.currency_conversion.domain.model.Currency
import pro.tanay.currency_conversion.domain.model.RequestState
import pro.tanay.currency_conversion.ext.roundTo

class MainViewModel(
    private val preferenceService: IPreferenceRepository,
    private val currencyApiService: ICurrencyApiRepository,
    private val database: CurrencyDatabase
) : ViewModel() {

    private val _state: MutableStateFlow<RequestState> = MutableStateFlow(RequestState.Loading)
    val state = _state.asStateFlow()

    private var currencyList = listOf<Currency>()

    fun getCurrencyList() = currencyList

    init {
        initialization()
    }

    private fun initialization() {
        viewModelScope.launch {
            preferenceService.shouldRefreshData()
                .flowOn(Dispatchers.Default)
                .collectLatest { lastRefreshedTime ->
                    // if value > 0 means: data is available else no data available, need to refresh
                    if (lastRefreshedTime > 0) {
                        val difference = Clock.System.now().epochSeconds - lastRefreshedTime
                        // if 30 minutes passed, refresh data
                        if (difference > 30 * 60) {
                            getDataFromApi()
                        } else {
                            getDataFromDatabase()
                        }
                    } else {
                        getDataFromApi()
                    }
                    // canceled this flow because after getting value one time, we don't want to observe
                    cancel()
                }
        }
    }

    private fun getDataFromApi() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val state = currencyApiService.getLatestExchangeRates()
                _state.emit(state)
                if (state is RequestState.Success) {
                    currencyList = state.data.currencyList
                }
            }
        }
    }

    private fun getDataFromDatabase() {
        viewModelScope.launch {
            database.currencyDao().getAllCurrencies()
                .flowOn(Dispatchers.Default)
                .collectLatest { list ->
                    currencyList = list
                    _state.emit(RequestState.Success(ApiResponse(list, mutableMapOf())))
                }
        }
    }

    fun process(input: String, baseCurrency: Currency) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val newList = mutableListOf<Currency>()
                currencyList.forEach { currency ->
                    val converted =
                        (currency.value / baseCurrency.value * input.toDouble()).roundTo(4)
                    newList.add(currency.copy(value = converted))
                }
                _state.emit(RequestState.Success(ApiResponse(newList, mutableMapOf())))
            }

            /*            database.currencyDao().getAllCurrencies()
                            .flowOn(Dispatchers.Default)
                            .collectLatest { list ->
                                val newList = mutableListOf<Currency>()
                                list.forEach { currency ->
                                    val converted =
                                        (currency.value / baseCurrency.value * input.toDouble()).roundTo(4)
                                    newList.add(currency.copy(value = converted))
                                }
                                _state.emit(RequestState.Success(ApiResponse(newList, mutableMapOf())))
                            }*/
        }
    }

    fun retry() {
        viewModelScope.launch {
            _state.emit(RequestState.Loading)
        }
        initialization()
    }
}