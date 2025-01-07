package at.florianschmid.fridgeventory.ExpiringItems

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import at.florianschmid.fridgeventory.ExpiringItems.theme.Typography
import at.florianschmid.fridgeventory.data.Item
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ExpiringItemsUI(modifier: Modifier = Modifier,
                    itemsViewModel: ExpiringViewModel = viewModel(factory = AppViewModelProvider.Factory),
                    ): Unit {

    val state by itemsViewModel.expiringItemsUiState.collectAsStateWithLifecycle()

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Expiring Items", style = Typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        LazyColumn {
            itemsIndexed(state.items) { _, item ->
                ItemCard (item)
            }
        }
    }
}

@Composable
fun ItemCard(item: Item) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.border (1.dp, color = Color.White)
                           .padding(8.dp)
                            .fillMaxWidth()) {

        Column {
            Text("Item Name: ${item.name}")
            Text("Expiry Date: ${DateTimeFormatter.ofPattern("dd.MM.yyyy").format(item.expiry_date)}")
        }
    }
}

@Composable
@Preview
fun ExpiringItemsUIPreview() {

    val mockItems = mutableListOf(
        Item(0, "Apples", LocalDateTime.of(2024, 12, 24, 0, 0), 5,"", ""),
    )

    ItemCard(mockItems[0])
}