package pro.tanay.currency_conversion.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ApiResponse(
    val timestamp: Long,
    val currencyList: MutableList<Currency>,
    val currencyMap: MutableMap<String, Double>
)

@Entity(tableName = "currency")
data class Currency(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val code: String,
    val value: Double
)