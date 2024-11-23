package pro.tanay.currency_conversion.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pro.tanay.currency_conversion.domain.model.Currency
import pro.tanay.currency_conversion.domain.model.RequestState
import pro.tanay.currency_conversion.viewmodel.MainViewModel

@Composable
fun GridUI(viewModel: MainViewModel) {
    val list = remember { mutableStateOf<List<Currency>>(mutableListOf()) }
    val state: RequestState by viewModel.state.collectAsStateWithLifecycle()
    val isError = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }

    when (state) {
        is RequestState.Error -> {
            errorMessage.value = (state as RequestState.Error).message
            isError.value = true
        }

        RequestState.Loading -> {
            ProgressBar()
        }

        is RequestState.Success -> {
            isError.value = false
            list.value = (state as RequestState.Success).data.currencyList
            GridView(list)
        }
    }

    if (isError.value) {
        ErrorView(errorMessage.value) {
            viewModel.retry()
        }
    }
}

@Composable
private fun GridView(list: MutableState<List<Currency>>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .padding(5.dp)
            .fillMaxSize()
            .navigationBarsPadding()
            .imePadding()
    ) {

        items(items = list.value, key = { item -> item.code }) { item ->
            GridItem(item)
        }
    }
}


@Composable
private fun GridItem(currency: Currency) {
    Card(
        elevation = CardDefaults.cardElevation(1.dp, 1.dp, 1.dp, 1.dp, 1.dp, 1.dp),
        modifier = Modifier.padding(5.dp)
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