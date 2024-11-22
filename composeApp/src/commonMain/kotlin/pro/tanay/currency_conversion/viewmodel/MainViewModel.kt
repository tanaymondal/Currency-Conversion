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
import pro.tanay.currency_conversion.domain.model.RequestState

class MainViewModel(
    private val preferenceService: IPreferenceRepository,
    private val currencyApiService: ICurrencyApiRepository,
    private val database: CurrencyDatabase
) : ViewModel() {

    private val _state: MutableStateFlow<RequestState> = MutableStateFlow(RequestState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            preferenceService.shouldRefreshData()
                .flowOn(Dispatchers.Default)
                .collectLatest { lastRefreshedTime ->
                    // if value > 0 means: data is available else no data available, need to refresh
                    if (lastRefreshedTime > 0) {
                        val difference = Clock.System.now().epochSeconds - lastRefreshedTime
                        println("LOG_CMP: ${difference / 60}")
                        // if 2 hour passed, refresh data
                        if (difference > 120 * 60) {
                            getDataFromApi()
                        } else {
                            getDataFromDatabase()
                        }
                    } else {
                        getDataFromApi()
                    }
                    // cancel this flow after getting value one time
                    cancel()
                }
        }
    }

    private fun getDataFromApi() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val state = currencyApiService.getLatestExchangeRates()
                _state.emit(state)
            }
        }
    }

    private fun getDataFromDatabase() {
        viewModelScope.launch {
            database.currencyDao().getAllCurrencies()
                .flowOn(Dispatchers.Default)
                .collectLatest { list ->
                    _state.emit(RequestState.Success(ApiResponse(0, list, mutableMapOf())))
                }
        }
    }
}