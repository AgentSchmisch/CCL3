package at.florianschmid.fridgeventory.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.florianschmid.fridgeventory.data.Item
import at.florianschmid.fridgeventory.data.ItemRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TaskViewModel(val repository: ItemRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.getAllItems()
        }
    }

    fun incrementCount(item: Item) {
        viewModelScope.launch {
            repository.updateItem(item.copy(quantity = item.quantity + 1))
        }
    }

    fun decrementCount(item: Item) {
        viewModelScope.launch {
            if (item.quantity > 0) { // Prevent negative quantities
                repository.updateItem(item.copy(quantity = item.quantity - 1))
            }
        }
    }

    val tasksUiState = repository.getAllItems()
        .map { TasksUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TasksUiState(emptyList())
        )
}
