package at.florianschmid.fridgeventory.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import at.florianschmid.fridgeventory.ItemApplication
import at.florianschmid.fridgeventory.ui.add.ItemAddViewModel
import at.florianschmid.fridgeventory.ui.edit.ItemEditViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            ItemViewModel((this[APPLICATION_KEY] as ItemApplication).itemRepository, (this[APPLICATION_KEY] as ItemApplication).applicationContext)
        }

        initializer {
            ItemUpdateViewModel(this.createSavedStateHandle(), (this[APPLICATION_KEY] as ItemApplication).itemRepository)
        }

        initializer {
            ItemEditViewModel(this.createSavedStateHandle(), (this[APPLICATION_KEY] as ItemApplication).itemRepository)
        }

        initializer {
            ItemAddViewModel((this[APPLICATION_KEY] as ItemApplication).itemRepository)
        }
    }
}