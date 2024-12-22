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

    val tasksUiState = repository.getAllItems()
        .map { TasksUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TasksUiState(emptyList())
        )

}
