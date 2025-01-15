package at.florianschmid.fridgeventory.ExpiringItems

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import at.florianschmid.fridgeventory.ui.theme.Typography
import at.florianschmid.fridgeventory.R
import at.florianschmid.fridgeventory.data.Item
import at.florianschmid.fridgeventory.data.remote.RemoteService
import at.florianschmid.fridgeventory.ui.LocalImageDisplay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ExpiringItemsUI(
    modifier: Modifier = Modifier,
    itemsViewModel: ExpiringViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    // Collect the state from the ViewModel
    val state by itemsViewModel.expiringItemsUiState.collectAsStateWithLifecycle()
    // Use mutableStateListOf for recipe items
    val recipeItems = remember { mutableStateListOf<Item>() }

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Expiring Items", style = Typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        LazyColumn {
            itemsIndexed(state.items) { _, item ->
                ItemCard(item = item, recipeItems = recipeItems)
            }
        }

        Spacer(Modifier.height(16.dp))
        CreateRecipeButton { onCreateRecipeClick(recipeItems) }
    }
}

@Composable
fun CreateRecipeButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Create Recipe")
    }
}

@Composable
fun ItemCard(item: Item, recipeItems: SnapshotStateList<Item>, modifier: Modifier = Modifier) {
    val isChecked = remember { mutableStateOf(item.checkedForRecipe) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LocalImageDisplay(item)

            // Overlay for item details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(colorResource(R.color.f_purple))
                    .padding(16.dp)
            ) {
                Text(
                    text = item.name,
                    style = Typography.headlineMedium,
                    color = Color.White
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_timer_24),
                        contentDescription = "Expiry Date",
                        tint = Color.White
                    )
                    Text(
                        text = DateTimeFormatter.ofPattern("dd.MM.yyyy").format(item.expiry_date),
                        style = Typography.bodyLarge,
                        color = Color.White
                    )
                }
            }

            // Quantity and checkbox
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Quantity: ${item.quantity}",
                    color = Color.White,
                    style = androidx.compose.ui.text.TextStyle(fontSize = 18.sp)
                )
                Checkbox(
                    checked = isChecked.value,
                    onCheckedChange = { checked ->
                        isChecked.value = checked
                        checkBoxChanged(item, recipeItems)
                    },
                    colors = CheckboxDefaults.colors(checkmarkColor = Color.White)
                )
            }
        }
    }
}

fun checkBoxChanged(item: Item, recipeItems: SnapshotStateList<Item>) {
    item.checkedForRecipe = !item.checkedForRecipe
    if (item.checkedForRecipe) {
        if (!recipeItems.contains(item)) recipeItems.add(item)
    } else {
        recipeItems.remove(item)
    }
}

fun onCreateRecipeClick(recipeItems: List<Item>) {
    // Simulate creating a recipe with selected items
    println("Creating recipe with: $recipeItems")
    // call the remote service to get recipes
    val recipeRecommendations = RemoteService().fetchRecipeRecommendations(recipeItems)

    // navigate to the recipe screen

}

@Composable
@Preview
fun ExpiringItemsUIPreview() {
    val mockItems = listOf(
        Item(0, "Apples", LocalDateTime.of(2024, 12, 24, 0, 0), 5, "", ""),
        Item(1, "Bananas", LocalDateTime.of(2024, 12, 20, 0, 0), 3, "", "")
    )
    val recipeItems = remember { mutableStateListOf<Item>() }

    Column {
        mockItems.forEach { item ->
            ItemCard(item = item, recipeItems = recipeItems)
        }
    }
}
