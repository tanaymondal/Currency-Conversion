package pro.tanay.currency_conversion.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pro.tanay.currency_conversion.domain.model.Currency

@Composable
fun CurrencyPickerDialog(
    currencies: List<Currency>,
    onDismiss: () -> Unit,
    onSelection: (Currency) -> Unit
) {

    AlertDialog(title = {


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Choose Input Currency",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }, text = {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier.height(250.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(currencies) { item ->
                    Card {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(2.dp)
                                .clickable { onSelection(item) },
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.code,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row {
                ElevatedButton(onClick = { onDismiss() }) {
                    Text(text = "Cancel")
                }
            }
        }


    }, onDismissRequest = {
        onDismiss()
    }, confirmButton = {

    }, modifier = Modifier.fillMaxWidth())
}