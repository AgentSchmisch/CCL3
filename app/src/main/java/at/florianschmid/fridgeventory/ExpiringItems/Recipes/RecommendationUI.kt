package at.florianschmid.fridgeventory.ExpiringItems.Recipes

import android.content.Intent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.IntentCompat.getParcelableArrayListExtra
import at.florianschmid.fridgeventory.data.Recipe
import coil3.compose.AsyncImage


fun RecipeUI(modifier: Modifier = Modifier, onClose: () -> Unit) {


}


@Composable
fun RecommendationCard(recipe: Recipe, modifier: Modifier = Modifier) {
    Card() {
        ImageDisplay(recipe)
        Row(){
            Text(recipe.image)
        }
    }
}


@Composable
fun ImageDisplay(recipe: Recipe) {
    AsyncImage(
        model = "https://img.spoonacular.com/recipes/673463-312x231.jpg",
        contentDescription = null,
    )
}


@Composable
@Preview
fun RecommendationPreview() {
    //val recipe = Recipe(parseToJsonElement("{\"id\": 1, \"title\": \"Test\", \"image\": \"https://img.spoonacular.com/recipes/673463-312x231.jpg\", \"usedIngredientCount\": 1, \"missedIngredientCount\": 1, \"missedIngredients\": [], \"usedIngredients\": [], \"unusedIngredients\": [], \"likes\": 1}"))
    //RecommendationCard(recipe)
}