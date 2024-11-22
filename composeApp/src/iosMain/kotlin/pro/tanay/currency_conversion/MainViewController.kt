package pro.tanay.currency_conversion

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import pro.tanay.currency_conversion.di.KoinInitializer

fun MainViewController() = ComposeUIViewController {
    configure = {
        KoinInitializer().init()
    }
    App()
}