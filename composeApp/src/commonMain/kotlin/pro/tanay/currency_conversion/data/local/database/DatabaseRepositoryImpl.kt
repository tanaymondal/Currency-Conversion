package pro.tanay.currency_conversion.data.local.database

import kotlinx.coroutines.flow.Flow
import pro.tanay.currency_conversion.domain.IDatabaseRepository
import pro.tanay.currency_conversion.domain.model.Currency

class DatabaseRepositoryImpl(private val db: CurrencyDatabase) : IDatabaseRepository {

    override fun getAllCurrencies(): Flow<List<Currency>> {
        return db.currencyDao().getAllCurrencies()
    }

    override suspend fun insertAll(list: MutableList<Currency>) {
        db.currencyDao().insertAll(list)
    }
}