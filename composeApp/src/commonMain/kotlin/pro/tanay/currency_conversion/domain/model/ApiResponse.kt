package pro.tanay.currency_conversion.domain.model

data class ApiResponse(
    val timestamp: Long,
    val currencyList: MutableList<Currency>
)

data class Currency(
    val code: String,
    val value: Double
)