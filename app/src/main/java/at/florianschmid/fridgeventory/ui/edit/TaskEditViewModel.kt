package at.florianschmid.fridgeventory.ui.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.florianschmid.fridgeventory.data.Item
import at.florianschmid.fridgeventory.data.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

data class ContactEditUi(
    val item: Item = Item(0, "", LocalDateTime.of(2024,12,9,0,0), 5,"")
)

class TaskEditViewModel(private val savedStateHandle: SavedStateHandle,
                        private val itemRepository: ItemRepository) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle["itemId"])

    var editUiState by mutableStateOf(ContactEditUi())
        private set

    init {
        viewModelScope.launch {
            val item = withContext(Dispatchers.IO) {
                itemRepository.findItemById(itemId)
            }
            editUiState = ContactEditUi(item)
        }
    }

    fun updateItem(item: Item) {
        editUiState = editUiState.copy(item=item)
    }

    fun saveItem() {
        viewModelScope.launch {
            itemRepository.updateItem(editUiState.item)
        }
    }

}