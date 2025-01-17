package at.florianschmid.fridgeventory.ExpiringItems.Recipes

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import at.florianschmid.fridgeventory.R
import at.florianschmid.fridgeventory.data.RecipeRecommendation
import coil3.compose.AsyncImage
import kotlinx.serialization.json.Json.Default.parseToJsonElement


@Composable
fun RecipeUI(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    recommendationViewModel: RecommendationViewModel = hiltViewModel()
) {
    val state by recommendationViewModel.recommendations.collectAsStateWithLifecycle()

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Recipes")
        Spacer(Modifier.height(16.dp))

        LazyColumn {
            itemsIndexed(state.items) { _, recipe ->
                RecommendationCard(recipe)
            }
        }
    }

}


@Composable
fun RecommendationCard(recipeRecommendation: RecipeRecommendation, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp) // Add padding around the card
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
                    text = recipeRecommendation.title,
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


@Composable
fun ImageDisplay(recipeRecommendation: RecipeRecommendation) {

    AsyncImage(
        model = recipeRecommendation.image,
        contentDescription = "Image of ${recipeRecommendation.title}",
    )




    Image(
        painter = painterResource(id = R.mipmap.recipe_foreground),
        contentDescription = "Apple",
        modifier = Modifier
    )
}


@Composable
@Preview
fun RecommendationPreview() {
    val recipeRecommendation =
        RecipeRecommendation(parseToJsonElement("{\"id\": 1, \"title\": \"Easy Homemade Apple Fritters\", \"image\": \"https://img.spoonacular.com/recipes/673463-312x231.jpg\", \"usedIngredientCount\": 1, \"missedIngredientCount\": 1, \"missedIngredients\": [], \"usedIngredients\": [], \"unusedIngredients\": [], \"likes\": 1}"))
    RecommendationCard(recipeRecommendation)
}