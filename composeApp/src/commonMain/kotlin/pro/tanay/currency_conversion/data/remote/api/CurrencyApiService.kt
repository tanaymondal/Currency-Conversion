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
import pro.tanay.currency_conversion.domain.CurrencyApiService
import pro.tanay.currency_conversion.domain.model.ApiResponse
import pro.tanay.currency_conversion.domain.model.Currency
import pro.tanay.currency_conversion.domain.model.RequestState
import kotlin.math.round

class CurrencyApiServiceImpl : CurrencyApiService {
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

    override suspend fun getLatestExchangeRates(): RequestState<ApiResponse> {

        return try {
            val response = httpClient.get(END_POINT)
            if (response.status.value == 200) {

                val jsonElement = Json.parseToJsonElement(response.bodyAsText())
                val timestamp = jsonElement.jsonObject[TIMESTAMP]
                val rates = jsonElement.jsonObject[RATES]

                val keys = (rates as JsonObject).keys

                val list = mutableListOf<Currency>()
                keys.forEach {
                    // rounding off to 2 decimal point
                    val rate = round(rates[it].toString().toDouble() * 100) / 100
                    list.add(Currency(it, rate))
                }

                RequestState.Success(ApiResponse(timestamp.toString().toLong(), list))
            } else {
                RequestState.Error(message = "HTTP Error Code: ${response.status}")
            }
        } catch (e: Exception) {
            RequestState.Error(message = e.message.toString())
        }

    }
}