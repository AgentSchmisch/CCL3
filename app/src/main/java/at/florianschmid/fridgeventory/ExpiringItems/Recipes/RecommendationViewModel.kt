package at.florianschmid.fridgeventory.ExpiringItems.Recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.florianschmid.fridgeventory.data.Item
import at.florianschmid.fridgeventory.data.Recipe
import at.florianschmid.fridgeventory.data.RecipeRecommendation
import at.florianschmid.fridgeventory.data.remote.RemoteService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecommendationUiState(
    var items: List<RecipeRecommendation> = emptyList()
)

data class RecipeUiState(
    var recipe: Recipe? = null
)

data class RecipeStepUiState(
    var step: Int = 0
)

@HiltViewModel
class RecommendationViewModel @Inject constructor(private val remoteService: RemoteService, private val recipeService: RecipeService) : ViewModel() {

    val recommendations = recipeService.recommendations

    fun fetchRecommendations(ingredients: List<Item>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recommendations = remoteService.fetchRecipeRecommendations(ingredients)
                recipeService.setRecommendations(recommendations)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

@HiltViewModel
class RecipeViewModel @Inject constructor(private val remoteService: RemoteService, private val recipeService: RecipeService) : ViewModel() {

    val recipe = recipeService.recipe
    val recipeStep = recipeService.recipeStep

    fun resetRecipeStep(){
        recipeService.resetRecipeStep()
    }

    fun fetchRecipe(recipeId: Int, recipeName: String, recipeImage: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recipe = remoteService.fetchRecipeDetails(recipeId, recipeName, recipeImage)
                recipeService.setRecipe(recipe)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun incrementRecipeStep(){
        recipeService.incrementRecipeStep()
    }
    fun decrementRecipeStep(){
        recipeService.decrementRecipeStep()
    }
}
