package at.florianschmid.fridgeventory.ui

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import at.florianschmid.fridgeventory.data.Item
import at.florianschmid.fridgeventory.ui.add.TaskAddScreen
import at.florianschmid.fridgeventory.ui.edit.TaskEditScreen
import at.florianschmid.fridgeventory.ExpiringItems.theme.Typography
import at.florianschmid.fridgeventory.R
import java.time.format.DateTimeFormatter

enum class ItemRoutes(val route: String) {
    Home("task/home"),
    Detail("task/details/{itemId}"),
    Edit("task/details/{itemId}/edit"),
    Add("task/new")
}

@Composable
fun TodoApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ItemRoutes.Home.route,
        modifier = modifier
    ) {
        composable(ItemRoutes.Home.route) {
            ContactsHomeScreen(
                onEditClick = {
                    navController.navigate(ItemRoutes.Edit.route.replace("{itemId}", "$it"))
                },
                onAddClick = {
                    navController.navigate(ItemRoutes.Add.route) // Navigate to Add route
                },
                onCardClick = {
                    navController.navigate(ItemRoutes.Edit.route.replace("{itemId}", "$it"))
                }
            )
        }
        composable (
            route = ItemRoutes.Detail.route,
            arguments = listOf(navArgument("itemId") {
                type = NavType.IntType
            })
        ) {
            TaskDetailsScreen()
        }

        composable(
            route = ItemRoutes.Add.route
        ) {
            TaskAddScreen {
                navController.navigateUp()
            }
        }

        composable(
            route = ItemRoutes.Edit.route,
            arguments = listOf(navArgument("itemId") {
                type = NavType.IntType
            })
        ) {
            TaskEditScreen {
                navController.navigateUp()
            }
        }
    }
}

@Composable
fun ContactsHomeScreen(
    modifier: Modifier = Modifier,
    taskViewModel: TaskViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onEditClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    onCardClick: (Int) -> Unit,
) {
    val state by taskViewModel.tasksUiState.collectAsStateWithLifecycle()

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        // We use lazy column for dynamic lists or large lists
        // it will only draw the visible contacts
        Text("Items", style = Typography.titleLarge)

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            itemsIndexed(state.items) { _, item ->
                TaskListItem(item,
                    onCardClick = {
                        onCardClick(item.id)
                    },
                    onEditClick = {
                        onEditClick(item.id)
                    },
                    viewModel = taskViewModel
                )
            }
        }

        AddTaskButton(onAddClick = onAddClick)


    }
}

@Composable
fun AddTaskButton(onAddClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            shape = CircleShape,
            color = colorResource(R.color.f_pink),
            shadowElevation = 8.dp,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(56.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onAddClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add task",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp) // Increase the size of the icon
                )
            }
        }
    }
}


@Composable
fun TaskListItem(
    item: Item,
    onCardClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel
) {
    Card(
        onClick = { onEditClick() },
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
            Image(
                painter = painterResource(id = R.mipmap.apple_foreground),
                contentDescription = "Apple",
                modifier = Modifier
                    .fillMaxSize() // Fill the Box entirely, no gaps
                    .align(Alignment.Center), // Center image
                contentScale = ContentScale.Crop // Crop image to fill card without distortion
            )

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
                    style = Typography.headlineMedium,
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
                IconButton(onClick = { changeQuantity(item = item, change = -1, viewModel=viewModel) }) {
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

                IconButton(onClick = { changeQuantity(item, 1, viewModel=viewModel) }) {
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
    item: Item,
    change: Int,
    viewModel: TaskViewModel
) {
    // Call ViewModel to handle quantity change
    if (change > 0) {
        viewModel.incrementCount(item)
    } else {
        viewModel.decrementCount(item)
    }
}


@Composable
fun TaskDetailsScreen(modifier: Modifier = Modifier, taskUpdateViewModel: TaskUpdateViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val detailUiState by taskUpdateViewModel.detailUiState.collectAsStateWithLifecycle()
    TaskDetails(detailUiState.item, modifier)
}


@Composable
fun TaskDetails(item: Item, modifier: Modifier = Modifier) {
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
                Text("Expiry Date: ${DateTimeFormatter.ofPattern("dd.MM.yyyy").format(item.expiry_date)}", style = Typography.headlineSmall)
            }
        }
    }
}
