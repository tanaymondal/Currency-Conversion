package pro.tanay.currency_conversion.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import pro.tanay.currency_conversion.domain.ILocalRepository
import pro.tanay.currency_conversion.domain.IRemoteRepository
import pro.tanay.currency_conversion.domain.model.ApiResponse
import pro.tanay.currency_conversion.domain.model.Currency
import pro.tanay.currency_conversion.domain.model.RequestState

class RemoteRepositoryImpl(
    private val localRepository: ILocalRepository,
) : IRemoteRepository {

    companion object {
        private const val API_ID = "885bcf9b8abf49cbafda6ce9636637c5"
        private const val END_POINT = "https://openexchangerates.org/api/latest.json?app_id=$API_ID"

        private const val RATES = "rates"
    }

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
        }
        install(DefaultRequest)
    }

    override suspend fun getLatestExchangeRates(): RequestState {
        try {
            val response = httpClient.get(END_POINT)
            if (response.status.value == 200) {

                val jsonElement = Json.parseToJsonElement(response.bodyAsText())
                val rates = jsonElement.jsonObject[RATES]

                val keys = (rates as JsonObject).keys

                val list = mutableListOf<Currency>()
                keys.forEach {
                    val rate = rates[it].toString().toDouble()
                    list.add(Currency(code = it, value = rate))
                }

                localRepository.preference().saveTimestamp(Clock.System.now().epochSeconds)

                localRepository.database().insertAll(list)

                return RequestState.Success(ApiResponse(list))
            } else {
                return RequestState.Error(message = "HTTP Error Code: ${response.status}")
            }
        } catch (e: Exception) {
            return RequestState.Error(message = e.message.toString())
        }

    }
}