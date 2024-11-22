package pro.tanay.currency_conversion.domain

import kotlinx.coroutines.flow.Flow

interface IPreferenceRepository {
    suspend fun saveTimestamp(timestamp: Long)
    fun shouldRefreshData(): Flow<Long>

}