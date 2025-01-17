package at.florianschmid.fridgeventory.ExpiringItems.Recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.florianschmid.fridgeventory.data.Item
import at.florianschmid.fridgeventory.data.ItemRepository
import at.florianschmid.fridgeventory.data.Recipe
import at.florianschmid.fridgeventory.data.RecommendationRepository
import at.florianschmid.fridgeventory.data.db.ItemDao
import at.florianschmid.fridgeventory.data.db.RecommendationDao
import at.florianschmid.fridgeventory.data.remote.RemoteService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecommendationUiState(
    var items: List<Recipe> = emptyList()
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
