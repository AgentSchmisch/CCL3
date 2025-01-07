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

data class ItemAddUi(
    val item: Item = Item(0, "", LocalDateTime.now(), 5,"","")
)

class ItemAddViewModel(private val itemRepository: ItemRepository) : ViewModel() {

    var addUiState by mutableStateOf(ItemAddUi())
        private set

    init {
        viewModelScope.launch {
            val item = Item(0, "", LocalDateTime.now(), 5,"","")
            addUiState = ItemAddUi(item)
        }
    }

    fun updateItem(item: Item) {
        addUiState = addUiState.copy(item = item)
    }

    fun saveItem() {
        viewModelScope.launch {
            itemRepository.addItem(addUiState.item)
        }
    }

}