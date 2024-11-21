package pro.tanay.currency_conversion.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.core.screen.Screen
import pro.tanay.currency_conversion.data.remote.api.CurrencyApiServiceImpl

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        LaunchedEffect(Unit) {
            val data = CurrencyApiServiceImpl().getLatestExchangeRates()
            println("API RESPONSE:  ${data.getSuccessData().currencyList}")
        }
    }
}