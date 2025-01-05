package at.florianschmid.fridgeventory.ExpiringItems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.florianschmid.fridgeventory.data.ItemRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExpiringViewModel(val repository: ItemRepository): ViewModel() {

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
}