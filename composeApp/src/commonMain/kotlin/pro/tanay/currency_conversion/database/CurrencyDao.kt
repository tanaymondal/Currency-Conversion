package pro.tanay.currency_conversion.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import pro.tanay.currency_conversion.domain.model.Currency

@Dao
interface CurrencyDao {

    @Upsert
    suspend fun upsert(currency: Currency)

    @Query("SELECT * FROM currency WHERE code = :code")
    suspend fun getCurrencyByCode(code: String): Currency

    @Query("SELECT * FROM currency")
    fun getAllCurrencies(): Flow<List<Currency>>

    @Upsert
    suspend fun upsertAll(list: MutableList<Currency>)
}