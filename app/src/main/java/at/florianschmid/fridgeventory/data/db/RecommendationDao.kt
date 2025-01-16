package at.florianschmid.fridgeventory.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RecommendationDao {

    @Insert
    suspend fun addItem(recommendation: RecommendationEntity)

    @Update
    suspend fun updateItem(recommendation: RecommendationEntity)

    @Delete
    suspend fun deleteItem(recommendation: RecommendationEntity)

    @Query("SELECT * FROM reciperecommendations WHERE _id = :id")
    suspend fun findItemById(id: Int): RecommendationEntity


}
