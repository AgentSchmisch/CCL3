package at.florianschmid.fridgeventory.data

import java.time.LocalDateTime

data class Item(
    val id: Int,
    val name: String,
    val expiry_date: LocalDateTime,
    var quantity: Int,
    val additional: String,
    var checked: Boolean = false
)
