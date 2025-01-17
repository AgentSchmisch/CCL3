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


