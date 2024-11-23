package pro.tanay.currency_conversion.domain

import pro.tanay.currency_conversion.domain.model.RequestState

interface ICurrencyApiRepository {

    suspend fun getLatestExchangeRates(): RequestState

}