package pro.tanay.currency_conversion.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pro.tanay.currency_conversion.domain.IPreferenceRepository

class PreferenceRepositoryImpl(private val dataStore: DataStore<Preferences>) :
    IPreferenceRepository {

    private val timestampKey = longPreferencesKey("timestamp")

    override suspend fun saveTimestamp(timestamp: Long) {
        dataStore.edit { ds ->
            ds[timestampKey] = timestamp
        }
    }

    override fun shouldRefreshData(): Flow<Long> {
        return dataStore
            .data
            .map {
                it[timestampKey] ?: 0
            }
    }

}