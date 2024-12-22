package at.florianschmid.fridgeventory.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.florianschmid.fridgeventory.data.Item
import at.florianschmid.fridgeventory.data.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

data class ItemDetailUi(
    val item: Item = Item(0, "", LocalDateTime.of(2024,12,9,0,0), 1, "")
)

class TaskUpdateViewModel(savedStateHandle: SavedStateHandle, private val itemRepository: ItemRepository): ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle["itemId"])

    private val _detailUiState = MutableStateFlow(ItemDetailUi())
    val detailUiState = _detailUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val item = withContext(Dispatchers.IO) {
                itemRepository.findItemById(itemId)
            }
            _detailUiState.update {
                ItemDetailUi(item)
            }
        }
    }

}