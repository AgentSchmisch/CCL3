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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class TaskViewModel(val repository: ItemRepository, val context: Context) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.getAllItems()
        }
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

        // Create the worker request
        val workRequest: WorkRequest = PeriodicWorkRequest.Builder(
            ExpiryNotificationWorker::class.java,
            30, // Interval duration
            TimeUnit.MINUTES // Interval time unit
        )
            .build()

        // Enqueue the worker
        WorkManager.getInstance(context).enqueue(workRequest)
    }

    val tasksUiState = repository.getAllItems()
        .map { TasksUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TasksUiState(emptyList())
        )
}
