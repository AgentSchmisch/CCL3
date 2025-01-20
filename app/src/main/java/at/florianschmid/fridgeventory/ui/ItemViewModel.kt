package at.florianschmid.fridgeventory.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import at.florianschmid.fridgeventory.ExpiryNotificationWorker
import at.florianschmid.fridgeventory.data.Item
import at.florianschmid.fridgeventory.data.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(private val repository: ItemRepository, private val workManager: WorkManager, private val workRequest: WorkRequest) : ViewModel() {

    init {
        enqueueExpiryNotificationWorker()
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

    private fun enqueueExpiryNotificationWorker() {
        // Enqueue the worker
        workManager.enqueue(workRequest)
    }

    val itemUiState = repository.getAllItems()
        .map { ItemUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ItemUiState(emptyList())
        )
}
