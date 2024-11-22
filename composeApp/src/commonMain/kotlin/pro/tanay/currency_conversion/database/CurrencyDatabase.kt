package pro.tanay.currency_conversion.database

import androidx.room.Database
import androidx.room.RoomDatabase
import pro.tanay.currency_conversion.domain.model.Currency

@Database(
    entities = [Currency::class],
    version = 1
)
abstract class CurrencyDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao

}