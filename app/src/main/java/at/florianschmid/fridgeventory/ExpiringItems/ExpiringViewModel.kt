package at.florianschmid.fridgeventory.ExpiringItems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.florianschmid.fridgeventory.ExpiringItems.Recipes.RecipeService
import at.florianschmid.fridgeventory.data.Item
import at.florianschmid.fridgeventory.data.ItemRepository
import at.florianschmid.fridgeventory.data.remote.RemoteService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpiringViewModel @Inject constructor(private val repository: ItemRepository, private val recipeService: RecipeService, private val remoteService: RemoteService): ViewModel() {

    init {
        viewModelScope.launch {
            repository.findItemsExpiringSoon()
        }
    }

    val expiringItemsUiState = repository.findItemsExpiringSoon()
        .map { ExpiringItemsUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ExpiringItemsUiState(emptyList())
        )

    suspend fun getRecipeRecommendations(recipeItems:List<Item>){
        val recipeRecommendations = remoteService.fetchRecipeRecommendations(recipeItems)
        recipeService.setRecommendations(recipeRecommendations)
    }
}