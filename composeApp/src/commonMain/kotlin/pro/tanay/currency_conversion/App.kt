package pro.tanay.currency_conversion

import DarkColors
import LightColors
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import pro.tanay.currency_conversion.ext.koinViewModel
import pro.tanay.currency_conversion.ui.pages.HomePage
import pro.tanay.currency_conversion.viewmodel.MainViewModel

@Composable
@Preview
fun App() {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    MaterialTheme(colorScheme = colors) {
        KoinContext {
            val navController = rememberNavController()
            NavHost(
                navController = navController, startDestination = "HomePage"
            ) {
                composable("HomePage") {
                    val viewModel = koinViewModel<MainViewModel>()
                    HomePage(viewModel)
                }
            }
        }
    }
}
