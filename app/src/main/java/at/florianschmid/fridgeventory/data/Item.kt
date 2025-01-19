package at.florianschmid.fridgeventory.data

import java.time.LocalDateTime

data class Item(
    val id: Int,
    val name: String,
    val expiry_date: LocalDateTime = LocalDateTime.now(),
    var quantity: Int = 0,
    val additional: String,
    var image_path: String,
    var checkedForRecipe: Boolean = false

    )
