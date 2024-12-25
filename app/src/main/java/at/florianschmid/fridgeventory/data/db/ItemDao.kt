package at.florianschmid.fridgeventory.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert
    suspend fun addItem(itemEntity: ItemEntity)

    @Update
    suspend fun updateItem(itemEntity: ItemEntity)

    @Delete
    suspend fun deleteItem(itemEntity: ItemEntity)

    @Query("SELECT * FROM items WHERE _id = :id")
    suspend fun findItemById(id: Int): ItemEntity

    @Query("SELECT * from items WHERE quantity > 0 ORDER BY expiry_date ASC ")
    fun getAllItems(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE expiry_date BETWEEN date('now') AND date('now', '+2 days') AND quantity > 0 ORDER BY expiry_date ASC")
    fun getItemsExpiringSoon(): Flow<List<ItemEntity>>

}
