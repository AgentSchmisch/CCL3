package at.florianschmid.fridgeventory.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    val name: String,
    val expiry_date:  LocalDateTime,
    var quantity: Int = 0,
    val additional: String,
    val image_path: String
)

@Entity(tableName = "reciperecommendations")
data class RecommendationEntity(
    @PrimaryKey(autoGenerate = false)
    val _id: Int,
    val title:String,
    val usedIngredients:Int,
    val missingIngredients:Int,
    val image_path: String
)