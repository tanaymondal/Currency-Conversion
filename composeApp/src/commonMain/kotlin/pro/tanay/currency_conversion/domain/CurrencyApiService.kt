package pro.tanay.currency_conversion.domain

import pro.tanay.currency_conversion.domain.model.ApiResponse
import pro.tanay.currency_conversion.domain.model.RequestState

interface CurrencyApiService {

    suspend fun getLatestExchangeRates(): RequestState<ApiResponse>

}