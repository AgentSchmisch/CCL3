package at.florianschmid.fridgeventory.ExpiringItems.Recipes

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import at.florianschmid.fridgeventory.R
import at.florianschmid.fridgeventory.data.RecipeRecommendation
import at.florianschmid.fridgeventory.ui.Routes
import at.florianschmid.fridgeventory.ui.theme.Typography
import coil3.compose.AsyncImage
import kotlinx.serialization.json.Json.Default.parseToJsonElement


@Composable
fun RecommendationUI(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    recommendationViewModel: RecommendationViewModel = hiltViewModel(),
    recipeViewModel: RecipeViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by recommendationViewModel.recommendations.collectAsStateWithLifecycle()
    val recipe by recipeViewModel.recipe.collectAsStateWithLifecycle()

    if (state.items == null || state.items.isEmpty()) {
        Column(
            modifier = modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            NotFound()
            Button(onClick = {
                navController.navigate(Routes.Expiring.route)
            }) {
                Text("Go to Expiring Items")
            }
        }

    } else {
        Box(modifier = modifier.fillMaxSize()) {

            Column(modifier = Modifier.fillMaxSize()) {

                Text(
                    "Recommended Recipes",
                    modifier = Modifier.padding(16.dp),
                    style = Typography.titleLarge,
                    color = colorResource(R.color.f_dark_purple)
                )

                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    LazyColumn {
                        itemsIndexed(state.items) { _, recipe ->
                            RecommendationCard(
                                recipe,
                                recipeViewModel,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotFound() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.notfound),
            contentDescription = "Not found",
            modifier = Modifier.size(500.dp)
        )
        Text(
            text = "You haven't generated any recipes yet\nGo to expiring Items to generate them",
            style = Typography.titleMedium,
            modifier = Modifier.padding(8.dp),
            color = colorResource(R.color.f_dark_purple)
        )
    }
}

@Composable
fun RecommendationCard(
    recipeRecommendation: RecipeRecommendation,
    recipeViewModel: RecipeViewModel,
    modifier: Modifier = Modifier,
    navController: NavController
) {
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
            modifier = Modifier
                .padding(16.dp), // Adjust padding for better spacing
            verticalAlignment = Alignment.CenterVertically // Align items vertically
        ) {
            // Image
            ImageDisplay(recipeRecommendation)

            // Content Column
            Column(
                modifier = Modifier
                    .weight(1f) // Let the column take up the remaining horizontal space
                    .padding(start = 16.dp), // Add spacing between image and content
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Recipe Name
                Text(
                    text = recipeRecommendation.name,
                    style = MaterialTheme.typography.bodyLarge, // Use Material Theme typography for consistency
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Ingredients Information
                Text(
                    text = "Used: ${recipeRecommendation.usedIngredientCount}, Missing: ${recipeRecommendation.missedIngredientCount}",
                    style = MaterialTheme.typography.bodySmall, // Smaller typography for secondary info
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Likes Section
            Column(
                modifier = Modifier.padding(start = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally, // Center items horizontally
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Likes",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = recipeRecommendation.likes.toString(),
                    style = MaterialTheme.typography.bodySmall, // Use small typography
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


fun onRecipeCardClick(
    recipeRecommendation: RecipeRecommendation,
    navController: NavController,
    recipeViewModel: RecipeViewModel
) {

    recipeViewModel.fetchRecipe(
        recipeRecommendation.id,
        recipeRecommendation.name,
        recipeRecommendation.image
    )
    recipeViewModel.resetRecipeStep()

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


@Composable
@Preview
fun NotFoundPreview() {
    NotFound()
}