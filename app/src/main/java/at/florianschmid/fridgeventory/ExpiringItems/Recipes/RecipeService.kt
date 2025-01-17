package at.florianschmid.fridgeventory.ExpiringItems.Recipes

import at.florianschmid.fridgeventory.data.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class RecipeService {
    private val _recommendations = MutableStateFlow(RecommendationUiState())

    val recommendations:StateFlow<RecommendationUiState> = _recommendations

    fun setRecommendations(recommendations: List<Recipe>) {
        _recommendations.update {
            it.copy(items = recommendations)
        }
    }

}