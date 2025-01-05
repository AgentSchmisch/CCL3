package at.florianschmid.fridgeventory.ExpiringItems

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import at.florianschmid.fridgeventory.ItemApplication


object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ExpiringViewModel((this[APPLICATION_KEY] as ItemApplication).itemRepository)
        }
    }
}