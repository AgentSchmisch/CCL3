package at.florianschmid.fridgeventory.ExpiringItems

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import at.florianschmid.fridgeventory.ExpiringItems.theme.Typography
import at.florianschmid.fridgeventory.R
import at.florianschmid.fridgeventory.data.Item
import at.florianschmid.fridgeventory.ui.LocalImageDisplay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ExpiringItemsUI(modifier: Modifier = Modifier,
                    itemsViewModel: ExpiringViewModel = viewModel(factory = AppViewModelProvider.Factory),
                    ): Unit {

    val state by itemsViewModel.expiringItemsUiState.collectAsStateWithLifecycle()
    val recipeItems by remember { mutableStateOf(mutableListOf<Item>()) }

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Expiring Items", style = Typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        LazyColumn {
            itemsIndexed(state.items) { _, item ->
                ItemCard (item, recipeItems)
            }
        }

        Spacer(Modifier.height(16.dp))
        createRecipeButton { onCreateRecipeClick(recipeItems) }
    }
}


@Composable
fun createRecipeButton(onClick: () -> Unit) {

    Button(onClick = (onClick)) {
        Text("Create Recipe")
    }

}

@Composable
fun ItemCard(item: Item, recipeItems: MutableList<Item>, modifier: Modifier = Modifier) {
    Card(
        onClick = {  },
        modifier = modifier
            .fillMaxWidth() // Card takes full width
            .height(240.dp) // Adjust height as needed
            .padding(8.dp), // Add padding around the card
        colors = CardDefaults.cardColors(containerColor = Color.Transparent), // Make container transparent
        shape = RoundedCornerShape(16.dp) // Optional: Rounded corners
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize() // Box stretches to fill the card
        ) {
            // Background Image that completely fills the card
            LocalImageDisplay(item)



                // Overlay content (Text and Controls)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart) // Align overlay to the bottom
                        .background(colorResource(R.color.f_purple))// Semi-transparent black background
                        .padding(16.dp) // Inner padding for overlay content
                ) {

                    // Title (Item Name)
                    Text(
                        text = item.name,
                        style = at.florianschmid.fridgeventory.ui.theme.Typography.headlineMedium,
                        color = Color.White
                    )

                    // Date and Timer Icon Row
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
                            text = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                                .format(item.expiry_date),
                            style = at.florianschmid.fridgeventory.ui.theme.Typography.bodyLarge,
                            color = Color.White
                        )
                    }
                }

                // Quantity
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.BottomEnd) // Align to bottom-right
                        .padding(16.dp)
                ) {

                    Text(
                        text = "Quantity: " + item.quantity.toString(),
                        color = Color.White,
                        style = androidx.compose.ui.text.TextStyle(fontSize = 18.sp)
                    )

                    Checkbox(checked = item.checkedForRecipe,
                        onCheckedChange = { checkBoxChanged(item = item) },  colors = CheckboxDefaults.colors(
                        checkedColor = Color.White, // Color when checked
                        uncheckedColor = Color.White, // Color when unchecked
                        checkmarkColor = Color.White // Color of the checkmark
                    ))
                }
        }
    }
}

fun checkBoxChanged(item: Item) {
    item.checkedForRecipe = !item.checkedForRecipe
}


fun onCreateRecipeClick(recipeItems: MutableList<Item>) {
    // Create Recipe
    println(recipeItems)
}

@Composable
@Preview
fun ExpiringItemsUIPreview() {

    val mockItems = mutableListOf(
        Item(0, "Apples", LocalDateTime.of(2024, 12, 24, 0, 0), 5,"", ""),
    )

    val recipeItems by remember { mutableStateOf(mockItems) }

    ItemCard(mockItems[0], recipeItems)
}