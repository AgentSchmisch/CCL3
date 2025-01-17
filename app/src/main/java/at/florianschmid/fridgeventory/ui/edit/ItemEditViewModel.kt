package at.florianschmid.fridgeventory.ui.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.florianschmid.fridgeventory.data.Item
import at.florianschmid.fridgeventory.data.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

data class ItemEditUi(
    val item: Item = Item(0, "", LocalDateTime.of(2024,12,9,0,0), 5,"","")
)
@HiltViewModel
class ItemEditViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle,
                                            private val itemRepository: ItemRepository) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle["itemId"])

    var editUiState by mutableStateOf(ItemEditUi())
        private set

    init {
        viewModelScope.launch {
            val item = withContext(Dispatchers.IO) {
                itemRepository.findItemById(itemId)
            }
            editUiState = ItemEditUi(item)
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