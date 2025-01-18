package at.florianschmid.fridgeventory.ExpiringItems.Recipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import at.florianschmid.fridgeventory.data.RecipeRecommendation
import at.florianschmid.fridgeventory.ui.Routes
import coil3.compose.AsyncImage


@Composable
fun RecommendationUI(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    recommendationViewModel: RecommendationViewModel = hiltViewModel(),
    recipeViewModel: RecipeViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by recommendationViewModel.recommendations.collectAsStateWithLifecycle()

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Recipes")
        Spacer(modifier.height(16.dp))

        LazyColumn {
            itemsIndexed(state.items) { _, recipe ->
                RecommendationCard(recipe, recipeViewModel, navController = navController)
            }
        }
    }
}


@Composable
fun RecommendationCard (recipeRecommendation: RecipeRecommendation, recipeViewModel: RecipeViewModel, modifier: Modifier = Modifier, navController:NavController) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = {
            onRecipeCardClick(
                recipeRecommendation = recipeRecommendation,
                navController = navController,
                recipeViewModel = recipeViewModel
            )
        }
    ) {
        Row(
            modifier = Modifier.padding(8.dp), // Padding inside the Row
            verticalAlignment = Alignment.CenterVertically // Align items vertically
        ) {
            // Image
            ImageDisplay(recipeRecommendation)

            // Column for details
            Column(
                modifier = Modifier
                    .weight(1f) // Take up the remaining horizontal space
                .padding(start = 8.dp), // Add spacing between image and content

                verticalArrangement = Arrangement.Center // Align items vertically

            ) {
                // Title
                Text(
                    text = recipeRecommendation.name,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)

                )
                Row (verticalAlignment = Alignment.CenterVertically) {
                Column() {
                    // Ingredients info
                    Text(
                        text = "Used Ingredients: ${recipeRecommendation.usedIngredientCount}",
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = "Missing Ingredients: ${recipeRecommendation.missedIngredientCount}",
                        modifier = Modifier.padding(8.dp)
                    )
                }
                    // Row for likes
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.Center, // Centers content horizontally
                        verticalAlignment = Alignment.CenterVertically // Centers content vertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Heart",
                            modifier = Modifier.size(24.dp) // Adjust icon size if needed
                        )
                        Spacer(Modifier.size(4.dp))
                        Text(recipeRecommendation.likes.toString(), modifier = Modifier.padding(end = 4.dp))
                    }
                }
            }
        }
    }
}


fun onRecipeCardClick(recipeRecommendation: RecipeRecommendation, navController: NavController, recipeViewModel: RecipeViewModel) {

    recipeViewModel.fetchRecipe(recipeRecommendation.id, recipeRecommendation.name, recipeRecommendation.image)

    navController.navigate(
        Routes.RecipeDetail.route.replace("{recipeId}", "${recipeRecommendation.id}")
    )
}


@Composable
fun ImageDisplay(recipeRecommendation: RecipeRecommendation) {
    AsyncImage(
        model = recipeRecommendation.image,
        contentDescription = "Image of ${recipeRecommendation.name}",
    )
}


