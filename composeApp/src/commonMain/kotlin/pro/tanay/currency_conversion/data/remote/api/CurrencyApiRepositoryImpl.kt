package pro.tanay.currency_conversion.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import pro.tanay.currency_conversion.database.CurrencyDatabase
import pro.tanay.currency_conversion.domain.ICurrencyApiRepository
import pro.tanay.currency_conversion.domain.IPreferenceRepository
import pro.tanay.currency_conversion.domain.model.ApiResponse
import pro.tanay.currency_conversion.domain.model.Currency
import pro.tanay.currency_conversion.domain.model.RequestState

class CurrencyApiRepositoryImpl(
    private val preferenceRepository: IPreferenceRepository,
    private val database: CurrencyDatabase
) :
    ICurrencyApiRepository {
    companion object {
        private const val API_ID = "885bcf9b8abf49cbafda6ce9636637c5"
        private const val END_POINT = "https://openexchangerates.org/api/latest.json?app_id=$API_ID"


        private const val TIMESTAMP = "timestamp"
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
                val timestamp = jsonElement.jsonObject[TIMESTAMP]
                val rates = jsonElement.jsonObject[RATES]

                val keys = (rates as JsonObject).keys

                val list = mutableListOf<Currency>()
                val map = mutableMapOf<String, Double>()
                keys.forEach {
                    // rounding off to 2 decimal point
                    //val rate = round(rates[it].toString().toDouble() * 100) / 100

                    val value = rates[it].toString().toDouble()
                    map[it] = value
                    list.add(Currency(code = it, value = value))
                }
                println("LOG_CMP Timestamp: ${timestamp.toString()}")
                preferenceRepository.saveTimestamp(timestamp.toString().toLong())

                database.currencyDao().upsertAll(list)

                return RequestState.Success(ApiResponse(timestamp.toString().toLong(), list, map))
            } else {
                return RequestState.Error(message = "HTTP Error Code: ${response.status}")
            }
        } catch (e: Exception) {
            return RequestState.Error(message = e.message.toString())
        }

    }
}