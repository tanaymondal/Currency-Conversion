package pro.tanay.currency_conversion.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ApiResponse(
    val currencyList: List<Currency>,
    val currencyMap: Map<String, Double>
)

@Entity(tableName = "currency")
data class Currency(
    @PrimaryKey
    val code: String,
    val value: Double
)