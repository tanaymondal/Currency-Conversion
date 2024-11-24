package pro.tanay.currency_conversion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import pro.tanay.currency_conversion.domain.IRepository
import pro.tanay.currency_conversion.domain.model.ApiResponse
import pro.tanay.currency_conversion.domain.model.Currency
import pro.tanay.currency_conversion.domain.model.RequestState
import pro.tanay.currency_conversion.ext.CoroutineDispatcherProvider
import pro.tanay.currency_conversion.ext.roundTo

class CurrencyViewModel(
    val repository: IRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : ViewModel() {

    private val _state: MutableStateFlow<RequestState> = MutableStateFlow(RequestState.Loading)
    val state = _state.asStateFlow()

    private var currencyList = listOf<Currency>()

    fun getCurrencyList(): List<Currency> {
        return currencyList
    }

    fun initialization() {
        viewModelScope.launch {
            repository.localRepository().preference().getTimestamp()
                .flowOn(dispatcher.default)
                .collectLatest { lastRefreshedTime ->
                    // if value > 0 means: data is available in db else no data available, need to refresh
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
            withContext(dispatcher.default) {
                val state = repository.remoteRepository().getLatestExchangeRates()
                _state.emit(state)
                if (state is RequestState.Success) {
                    currencyList = state.data.currencyList
                }
            }
        }
    }

    private fun getDataFromDatabase() {
        viewModelScope.launch {
            repository.localRepository().database().getAllCurrencies()
                .flowOn(dispatcher.default)
                .collectLatest { list ->
                    currencyList = list
                    _state.emit(RequestState.Success(ApiResponse(list)))
                }
        }
    }

    fun process(input: String, baseCurrency: Currency) {
        viewModelScope.launch {
            withContext(dispatcher.default) {
                val newList = mutableListOf<Currency>()
                currencyList.forEach { currency ->
                    val converted =
                        (currency.value / baseCurrency.value * input.toDouble()).roundTo(4)
                    newList.add(currency.copy(value = converted))
                }
                _state.emit(RequestState.Success(ApiResponse(newList)))
            }
        }
    }

    fun retry() {
        viewModelScope.launch {
            _state.emit(RequestState.Loading)
        }
        initialization()
    }
}