package pro.tanay.currency_conversion.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import co.touchlab.kermit.Logger
import pro.tanay.currency_conversion.domain.model.Currency
import pro.tanay.currency_conversion.ext.getValidInput
import pro.tanay.currency_conversion.ui.components.CurrencyPickerDialog
import pro.tanay.currency_conversion.ui.components.GridUI
import pro.tanay.currency_conversion.ui.components.TopHeaderView
import pro.tanay.currency_conversion.viewmodel.MainViewModel
import surfaceColor

@Composable
fun HomePage(viewModel: MainViewModel) {
    Logger.e(tag = "PAYPAY", messageString = "MainUI")
    val defaultCurrency = Currency(0, "USD", 1.0)
    val inputText = remember { mutableStateOf("1") }
    val currency = remember { mutableStateOf(defaultCurrency) }
    val isDialogOpened = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopHeaderView(
            currency.value,
            inputText,
            onInputChange = {
                Logger.e(tag = "PAYPAY", messageString = "onInputChange")
                if (viewModel.getCurrencyList().isNotEmpty()) {
                    inputText.value = it.getValidInput()

                    viewModel.process(
                        input = inputText.value.ifEmpty { 0.toString() },
                        baseCurrency = currency.value
                    )
                }
            },
            onCurrencyButtonClick = {
                Logger.e(tag = "PAYPAY", messageString = "onCurrencyChange")
                if (viewModel.getCurrencyList().isNotEmpty()) {
                    isDialogOpened.value = true
                }
            }
        )

        Logger.e(tag = "PAYPAY", messageString = "GridUI(viewModel)")
        GridUI(viewModel)
    }

    if (isDialogOpened.value) {
        CurrencyPickerDialog(
            viewModel.getCurrencyList(), onDismiss = {
                isDialogOpened.value = false
            },
            onSelection = { selectedCurrency ->
                currency.value = selectedCurrency
                isDialogOpened.value = false
                viewModel.process(
                    input = inputText.value.ifEmpty { 0.toString() },
                    baseCurrency = currency.value
                )
            }
        )
    }
}