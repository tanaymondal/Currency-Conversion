package pro.tanay.currency_conversion.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import pro.tanay.currency_conversion.data.local.database.CurrencyDatabase

fun createCurrencyDatabase(context: Context): CurrencyDatabase {
    val db = context.getDatabasePath("currency.db")
    return Room.databaseBuilder<CurrencyDatabase>(
        context = context, name = db.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}