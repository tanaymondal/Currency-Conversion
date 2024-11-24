package pro.tanay.currency_conversion.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import pro.tanay.currency_conversion.domain.model.Currency

@Database(
    entities = [Currency::class],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<CurrencyDatabase> {
    override fun initialize(): CurrencyDatabase
}