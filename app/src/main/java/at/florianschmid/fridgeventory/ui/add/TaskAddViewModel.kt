package at.florianschmid.fridgeventory.ui.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.florianschmid.fridgeventory.data.Item
import at.florianschmid.fridgeventory.data.ItemRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class TaskAddUi(
    val item: Item = Item(0, "", LocalDateTime.now(), 5,"")
)

class TaskAddViewModel(private val itemRepository: ItemRepository) : ViewModel() {

    var addUiState by mutableStateOf(TaskAddUi())
        private set

    init {
        viewModelScope.launch {
            val item = Item(0, "", LocalDateTime.now(), 5,"")
            addUiState = TaskAddUi(item)
        }
    }

    fun updateTask(item: Item) {
        addUiState = addUiState.copy(item = item)
    }

    fun saveTask() {
        viewModelScope.launch {
            itemRepository.addItem(addUiState.item)
        }
    }

}