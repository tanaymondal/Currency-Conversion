package pro.tanay.currency_conversion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import pro.tanay.currency_conversion.database.CurrencyDatabase
import pro.tanay.currency_conversion.domain.ICurrencyApiRepository
import pro.tanay.currency_conversion.domain.IPreferenceRepository
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
            preferenceService.shouldRefreshData().collectLatest { lastRefreshedTime ->
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
                cancel()
            }
        }
    }

    private fun getDataFromApi() {
        viewModelScope.launch {
            println("LOG_CMP: getDataFromApi")
            val state = currencyApiService.getLatestExchangeRates()
            _state.emit(state)
        }
    }

    private fun getDataFromDatabase() {
        println("LOG_CMP: getDataFromDatabase")
        viewModelScope.launch {

        }
    }
}