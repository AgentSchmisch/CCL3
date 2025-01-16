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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RecommendationUiState(
    var items: List<Recipe> = emptyList()
)

class RecommendationViewModel(private val remoteService: RemoteService = RemoteService(), val repository: RecommendationRepository) : ViewModel() {

    private val _recommendationUiState = MutableStateFlow(RecommendationUiState())
    val recommendationUiState: StateFlow<RecommendationUiState> = _recommendationUiState

    fun setRecommendation(recommendations: List<Recipe>) {
        _recommendationUiState.update {
            it.copy(items = recommendations)
        }
    }

    fun fetchRecommendations(ingredients: List<Item>) {
        viewModelScope.launch {
            try {
                val recommendations = remoteService.fetchRecipeRecommendations(ingredients)
                for (recipe in recommendations) {
                    repository.addItem(recipe)
                }
                setRecommendation(recommendations)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
