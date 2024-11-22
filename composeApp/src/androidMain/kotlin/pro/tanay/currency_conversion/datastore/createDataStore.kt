package pro.tanay.currency_conversion.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import pro.tanay.currency_conversion.DATA_STORE_FILE_NAME

fun createDataStore(context: Context): DataStore<Preferences> =
    pro.tanay.currency_conversion.createDataStore(
        producePath = { context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath }
    )