package at.florianschmid.fridgeventory.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import at.florianschmid.fridgeventory.ItemApplication
import at.florianschmid.fridgeventory.ui.add.TaskAddViewModel
import at.florianschmid.fridgeventory.ui.edit.TaskEditViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            TaskViewModel((this[APPLICATION_KEY] as ItemApplication).itemRepository, (this[APPLICATION_KEY] as ItemApplication).applicationContext)
        }

        initializer {
            TaskUpdateViewModel(this.createSavedStateHandle(), (this[APPLICATION_KEY] as ItemApplication).itemRepository)
        }

        initializer {
            TaskEditViewModel(this.createSavedStateHandle(), (this[APPLICATION_KEY] as ItemApplication).itemRepository)
        }

        initializer {
            TaskAddViewModel((this[APPLICATION_KEY] as ItemApplication).itemRepository)
        }
    }
}