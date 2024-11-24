package pro.tanay.currency_conversion.domain

import kotlinx.coroutines.flow.Flow
import pro.tanay.currency_conversion.domain.model.Currency

interface IDatabaseRepository {
    fun getAllCurrencies(): Flow<List<Currency>>
    suspend fun insertAll(list: MutableList<Currency>)
}