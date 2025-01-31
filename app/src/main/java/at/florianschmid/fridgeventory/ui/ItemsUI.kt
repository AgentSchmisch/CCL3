package at.florianschmid.fridgeventory.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import at.florianschmid.fridgeventory.ExpiringItems.ExpiringItemsUI
import at.florianschmid.fridgeventory.ExpiringItems.Recipes.RecipeUI
import at.florianschmid.fridgeventory.ExpiringItems.Recipes.RecommendationUI
import at.florianschmid.fridgeventory.data.Item
import at.florianschmid.fridgeventory.ui.add.ItemAddScreen
import at.florianschmid.fridgeventory.ui.edit.ItemEditScreen
import at.florianschmid.fridgeventory.ui.theme.Typography
import at.florianschmid.fridgeventory.R
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import java.time.format.DateTimeFormatter


enum class Routes(val route: String) {

    Home("fridgeventory/home"),
    Detail("fridgeventory/details/{itemId}"),
    Edit("fridgeventory/details/{itemId}/edit"),
    Add("fridgeventory/new"),
    Expiring("fridgeventory/expiring"),
    RecipeRecommendations("fridgeventory/recipes/recommendations"),
    RecipeDetail("fridgeventory/recipes/{recipeId}")

}


@Composable
fun FridgeventoryApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = { Footer(navController) }) { innerPadding ->

        NavHost(
            navController = navController, startDestination = Routes.Home.route, modifier = modifier
        ) {
            composable(Routes.Home.route) {
                ItemsHomeScreen(onEditClick = {
                    navController.navigate(Routes.Edit.route.replace("{itemId}", "$it"))
                }, onAddClick = {
                    navController.navigate(Routes.Add.route) // Navigate to Add route
                }, modifier = Modifier.padding(innerPadding)
                )
            }
            composable(
                route = Routes.Detail.route, arguments = listOf(navArgument("itemId") {
                    type = NavType.IntType
                })
            ) {
                ItemsDetailsScreen()
            }

            composable(
                route = Routes.Add.route
            ) {
                ItemAddScreen(
                    onSave = {
                        navController.navigateUp()
                    }, modifier = Modifier.padding(innerPadding)

                )
            }

            composable(
                route = Routes.Edit.route, arguments = listOf(navArgument("itemId") {
                    type = NavType.IntType
                })
            ) {
                ItemEditScreen {
                    navController.navigateUp()
                }
            }

            composable(
                route = Routes.Expiring.route
            ) {
                ExpiringItemsUI(
                    navController = navController, modifier = Modifier.padding(innerPadding)
                )
            }

            composable(
                route = Routes.RecipeDetail.route, arguments = listOf(navArgument("recipeId") {
                    type = NavType.IntType
                })
            ) {
                RecipeUI(
                    navController = navController, modifier = Modifier.padding(innerPadding)

                )
            }
            composable(route = Routes.RecipeRecommendations.route) {
                RecommendationUI(
                    navController = navController,
                    onClose = { navController.navigateUp() },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}


@Composable
fun ItemsHomeScreen(
    modifier: Modifier = Modifier,
    itemViewModel: ItemViewModel = hiltViewModel(),
    onEditClick: (Int) -> Unit,
    onAddClick: () -> Unit,
) {
    val state by itemViewModel.itemUiState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        // LazyColumn is the only scrollable component now
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header item at the top
            item {
                Text(
                    "Fridgeventory",
                    style = Typography.titleLarge,
                    modifier = Modifier.padding(16.dp),
                    color = colorResource(R.color.f_dark_purple)
                )
            }

            // List of items
            itemsIndexed(state.items) { _, item ->
                FridgeListItem(
                    item, onEditClick = { onEditClick(item.id) }, viewModel = itemViewModel
                )
            }

            // Additional space after the list
            item {
                Spacer(Modifier.height(50.dp))
            }
        }

        // Floating AddItemButton positioned at the bottom-right corner
        AddItemButton(onAddClick)
    }
}




@Composable
fun AddItemButton(onAddClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Floating Surface (Add Button) at the bottom-right corner
        Surface(
            shape = CircleShape,
            color = colorResource(R.color.f_pink),
            shadowElevation = 8.dp,
            modifier = Modifier
                .align(Alignment.BottomEnd) // Position the button at the bottom-right corner
                .padding(16.dp) // Padding around the button
                .size(56.dp) // Fixed size for the button to avoid squishing
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onAddClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add item",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp) // Icon size inside the button
                )
                Spacer(modifier = Modifier.width(56.dp)) // Add space between icon and text
            }
        }
    }
}

@Composable
fun FridgeListItem(
    item: Item, onEditClick: () -> Unit, modifier: Modifier = Modifier, viewModel: ItemViewModel
) {
    Card(
        onClick = { onEditClick() }, modifier = modifier
            .fillMaxWidth() // Card takes full width
            .height(240.dp) // Adjust height as needed
            .padding(8.dp), // Add padding around the card
        colors = CardDefaults.cardColors(containerColor = Color.Transparent), // Make container transparent
        shape = RoundedCornerShape(16.dp) // Optional: Rounded corners
    ) {
        Box(
            modifier = Modifier.fillMaxSize() // Box stretches to fill the card
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
                    text = item.name, style = Typography.headlineMedium, color = Color.White
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
                        text = DateTimeFormatter.ofPattern("dd.MM.yyyy").format(item.expiry_date),
                        style = Typography.bodyLarge,
                        color = Color.White
                    )
                }
            }

            // Controls (Quantity Increase/Decrease Buttons)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.BottomEnd) // Align to bottom-right
                    .padding(16.dp)
            ) {
                IconButton(onClick = {
                    changeQuantity(
                        item = item, change = -1, viewModel = viewModel
                    )
                }) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Decrease",
                        tint = Color.White
                    )
                }

                Text(
                    text = item.quantity.toString(), // Replace with dynamic value
                    color = Color.White,
                    style = androidx.compose.ui.text.TextStyle(fontSize = 18.sp)
                )

                IconButton(onClick = { changeQuantity(item, 1, viewModel = viewModel) }) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = "Increase",
                        tint = Color.White
                    )
                }
            }
        }
    }
}


fun changeQuantity(
    item: Item, change: Int, viewModel: ItemViewModel
) {
    // Call ViewModel to handle quantity change
    if (change > 0) {
        viewModel.incrementCount(item)
    } else {
        viewModel.decrementCount(item)
    }
}

@Composable
fun LocalImageDisplay(item: Item) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(item.image_path).crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.apple_background),
        contentDescription = "Image of ${item.name}",
        contentScale = ContentScale.Crop,
    )
}


@Composable
fun ItemsDetailsScreen(
    modifier: Modifier = Modifier, itemUpdateViewModel: ItemUpdateViewModel = hiltViewModel()
) {
    val detailUiState by itemUpdateViewModel.detailUiState.collectAsStateWithLifecycle()
    ItemDetails(detailUiState.item, modifier)
}


@Composable
fun ItemDetails(item: Item, modifier: Modifier = Modifier) {
    OutlinedCard(
        modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(item.name, style = Typography.headlineMedium)
            Column {
                Text("Description: ${item.additional}", style = Typography.headlineSmall)
                Spacer(Modifier.width(20.dp))
                Text(
                    "Expiry Date: ${
                        DateTimeFormatter.ofPattern("dd.MM.yyyy").format(item.expiry_date)
                    }", style = Typography.headlineSmall
                )
            }
        }
    }
}

@Composable
fun Footer(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val currentScreen = Routes.values().find { it.route == currentRoute } ?: Routes.Home

    NavigationBar(
        containerColor = colorResource(R.color.f_dark_purple)
    ) {
        NavigationBarItem(onClick = { navController.navigate(Routes.Home.route) }, icon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_home_24),
                contentDescription = "Home"
            )
        }, selected = currentScreen == Routes.Home
        )

        NavigationBarItem(onClick = { navController.navigate(Routes.Expiring.route) }, icon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_access_time_24),
                contentDescription = "Expiring Items"
            )
        }, selected = currentScreen == Routes.Expiring
        )

        NavigationBarItem(onClick = { navController.navigate(Routes.RecipeRecommendations.route) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_soup_kitchen_24),
                    contentDescription = "Recipes"
                )
            },
            selected = currentScreen == Routes.RecipeRecommendations || currentScreen == Routes.RecipeDetail
        )
    }
}


@Composable
@Preview
fun AddItemButtonPreview() {
    AddItemButton {}
}

@Composable
@Preview
fun FooterPreview() {
    Footer(rememberNavController())
}
