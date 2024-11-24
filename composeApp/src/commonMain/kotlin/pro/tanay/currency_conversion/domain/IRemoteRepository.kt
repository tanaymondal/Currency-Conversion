package pro.tanay.currency_conversion.domain

import pro.tanay.currency_conversion.domain.model.RequestState

interface IRemoteRepository {
    suspend fun getLatestExchangeRates(): RequestState
}