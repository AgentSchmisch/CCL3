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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import at.florianschmid.fridgeventory.ExpiringItems.Recipes.RecommendationViewModel
import at.florianschmid.fridgeventory.ui.theme.Typography
import at.florianschmid.fridgeventory.R
import at.florianschmid.fridgeventory.data.Item
import at.florianschmid.fridgeventory.ui.LocalImageDisplay
import at.florianschmid.fridgeventory.ui.Routes
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ExpiringItemsUI(
    modifier: Modifier = Modifier,
    itemsViewModel: ExpiringViewModel = hiltViewModel(),
    recommendationViewModel: RecommendationViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by itemsViewModel.expiringItemsUiState.collectAsStateWithLifecycle()

    val recipeItems = remember { mutableStateListOf<Item>() }

    if (state.items == null || state.items.isEmpty()) {
        Column {
            NotFound()
        }
    } else {
        Text(
            "Expiring Items",
            style = Typography.titleLarge,
            modifier = Modifier.padding(16.dp),
            color = colorResource(R.color.f_dark_purple)
        )
        Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(16.dp))

            LazyColumn {
                itemsIndexed(state.items) { _, item ->
                    ItemCard(item = item, recipeItems = recipeItems)
                }
            }

            Spacer(Modifier.height(16.dp))
            CreateRecipeButton {
                onCreateRecipeClick(recipeItems, recommendationViewModel, navController)
            }
        }
    }
}

@Composable
fun NotFound() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "No items found", style = Typography.titleMedium, color = Color.Gray
        )
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
                    text = item.name, style = Typography.headlineMedium, color = Color.White
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
                    checked = isChecked.value, onCheckedChange = { checked ->
                        isChecked.value = checked
                        checkBoxChanged(item, recipeItems)
                    }, colors = CheckboxDefaults.colors(checkmarkColor = Color.White)
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

fun onCreateRecipeClick(
    recipeItems: List<Item>,
    recommendationsViewModel: RecommendationViewModel,
    navController: NavController
) {
    // Simulate creating a recipe with selected items
    println("Creating recipe with: $recipeItems")
    recommendationsViewModel.fetchRecommendations(recipeItems)

    navController.navigate(
        Routes.RecipeRecommendations.route
    )

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
