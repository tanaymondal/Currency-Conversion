package pro.tanay.currency_conversion

import DarkColors
import LightColors
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
                    val viewModel = koinViewModel<MainViewModel>()
                    val state: RequestState by viewModel.state.collectAsStateWithLifecycle()


                }
            }
        }
    }

    // This is stateful composable function. This holds state.
    /*@Composable
    fun Conversation(list: MutableList<Currency> = remember { viewModel.list.toMutableStateList() }) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(start = 5.dp, end = 5.dp)
        ) {
            itemsIndexed(items = list, key = { index, message -> message.id }) { index, item ->

                var isSelected by rememberSaveable { mutableStateOf(item.isSelected) }

                StatelessRow(item,
                    isSelected,
                    onDelete = { list.removeAt(index) },
                    onButtonClick = {
                        isSelected = !isSelected
                        item.isSelected = !item.isSelected
                    })
            }
        }
    }


    // This is stateless composable function. This does not hold any state. We can reuse and test easily.
    @Composable
    private fun StatelessRow(
        message: Message, isSelected: Boolean, onDelete: () -> Unit, onButtonClick: () -> Unit
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium, shadowElevation = 5.dp
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(R.drawable.cat),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )

                    Column(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Text(
                            text = "Hello ${message.name}!",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = message.message,
                            color = MaterialTheme.colorScheme.tertiary,
                            style = MaterialTheme.typography.bodyMedium
                        )

                    }

                }

                ElevatedButton(onClick = if (isSelected) onDelete else onButtonClick) {
                    Text(if (isSelected) "Delete" else "Select")
                }
            }
        }
    }*/
}

@Composable
inline fun <reified T : ViewModel> koinViewModel(): T {
    val scope = currentKoinScope()
    return viewModel {
        scope.get<T>()
    }
}