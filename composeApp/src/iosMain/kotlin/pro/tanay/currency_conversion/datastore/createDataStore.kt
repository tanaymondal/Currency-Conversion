package pro.tanay.currency_conversion.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import pro.tanay.currency_conversion.DATA_STORE_FILE_NAME

fun createDataStore(): DataStore<Preferences> = pro.tanay.currency_conversion.createDataStore(
    producePath = {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        requireNotNull(documentDirectory).path + "/$DATA_STORE_FILE_NAME"
    }
)