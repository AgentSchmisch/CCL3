package at.florianschmid.fridgeventory.ExpiringItems.Recipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun RecipeUI(
    recipeViewModel: RecipeViewModel = hiltViewModel(),
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val state by recipeViewModel.recipe.collectAsStateWithLifecycle()

    val recipe = state.recipe

    Box(modifier) {

        Text("Recipe")
        Text(recipe?.name ?: "No Recipe")
        RecipeStepUI()
    }
}


@Composable
fun RecipeStepUI() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        IconButton(onClick = {  }) {
            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Next Step")
        }

        IconButton(onClick = {  }) {
            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next Step")
        }


    }
}

@Composable
fun onStepClicked(recipeViewModel: RecipeViewModel= hiltViewModel(), direction:Int){
    if(direction == 1){
        recipeViewModel.incrementRecipeStep()
    }
    else {
        recipeViewModel.decrementRecipeStep()
    }

}



@Preview
@Composable
fun RecipeUIPreview() {
    RecipeStepUI()
}


