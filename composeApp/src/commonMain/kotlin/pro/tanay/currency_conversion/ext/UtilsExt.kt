package pro.tanay.currency_conversion.ext

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.compose.currentKoinScope


@Composable
inline fun <reified T : ViewModel> koinViewModel(): T {
    val scope = currentKoinScope()
    return viewModel {
        scope.get<T>()
    }
}

fun String.getValidInput(): String {
    return if (this.toDoubleOrNull() == null) {
        ""
    } else {
        this
    }
}