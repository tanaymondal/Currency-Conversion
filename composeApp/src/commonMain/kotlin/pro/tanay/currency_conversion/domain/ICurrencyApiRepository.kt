package pro.tanay.currency_conversion.domain

import kotlinx.coroutines.flow.Flow
import pro.tanay.currency_conversion.domain.model.ApiResponse
import pro.tanay.currency_conversion.domain.model.RequestState

interface ICurrencyApiRepository {

    suspend fun getLatestExchangeRates(): RequestState

}