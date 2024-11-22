package pro.tanay.currency_conversion

import DarkColors
import LightColors
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.currentKoinScope
import pro.tanay.currency_conversion.domain.model.Currency
import pro.tanay.currency_conversion.domain.model.RequestState
import pro.tanay.currency_conversion.viewmodel.MainViewModel

@Composable
@Preview
fun App() {
    val colors = if (isSystemInDarkTheme()) LightColors else DarkColors
    MaterialTheme(colorScheme = colors) {
        KoinContext {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "HomePage"
            ) {
                composable("HomePage") {
                    Conversation()
                }
            }
        }
    }


}

// This is stateful composable function. This holds state.
@Composable
fun Conversation() {

    val viewModel = koinViewModel<MainViewModel>()
    val state: RequestState by viewModel.state.collectAsStateWithLifecycle()

    when (state) {
        is RequestState.Error -> {

        }

        RequestState.Loading -> {

        }

//        verticalArrangement = Arrangement.spacedBy(8.dp),
//        modifier = Modifier.padding(start = 5.dp, end = 5.dp)

        is RequestState.Success -> {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
            ) {

                val list = (state as RequestState.Success).data.currencyList
                items(items = list, key = { item -> item.code }) { item ->
                    StatelessRow(item)
                }
            }
        }
    }
}


// This is stateless composable function. This does not hold any state. We can reuse and test easily.
@Composable
private fun StatelessRow(currency: Currency) {
    Surface(
        shape = MaterialTheme.shapes.medium, shadowElevation = 5.dp
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(5.dp).fillMaxWidth()
        ) {
            Text(
                text = currency.code,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = currency.value.toString(),
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.bodyMedium
            )

        }
    }
}

@Composable
inline fun <reified T : ViewModel> koinViewModel(): T {
    val scope = currentKoinScope()
    return viewModel {
        scope.get<T>()
    }
}