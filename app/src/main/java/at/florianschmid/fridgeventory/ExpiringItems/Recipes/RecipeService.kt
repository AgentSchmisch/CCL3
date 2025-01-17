package at.florianschmid.fridgeventory.ExpiringItems.Recipes

import at.florianschmid.fridgeventory.data.Recipe
import at.florianschmid.fridgeventory.data.RecipeRecommendation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class RecipeService {
    private val _recommendations = MutableStateFlow(RecommendationUiState())
    private val _recipe = MutableStateFlow(RecipeUiState())
    val recommendations:StateFlow<RecommendationUiState> = _recommendations
    val recipe:StateFlow<RecipeUiState> = _recipe

    fun setRecommendations(recommendations: List<RecipeRecommendation>) {
        _recommendations.update {
            it.copy(items = recommendations)
        }
    }
    fun setRecipe(recipe: Recipe) {
        _recipe.update {
            it.copy(recipe = recipe)
        }
    }
}