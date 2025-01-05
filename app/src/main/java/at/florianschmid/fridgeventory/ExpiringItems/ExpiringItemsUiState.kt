package at.florianschmid.fridgeventory.ExpiringItems

import at.florianschmid.fridgeventory.data.Item

data class ExpiringItemsUiState(
    val items : List<Item>
)
