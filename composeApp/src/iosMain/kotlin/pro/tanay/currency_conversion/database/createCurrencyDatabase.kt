package pro.tanay.currency_conversion.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

fun createCurrencyDatabase(): CurrencyDatabase {
    val db = NSHomeDirectory() + "/people.db"
    return Room.databaseBuilder<CurrencyDatabase>(
        name = db,
        factory = { CurrencyDatabase::class.instantiateImpl() }
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}