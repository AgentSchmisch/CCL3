package at.florianschmid.fridgeventory.data

import at.florianschmid.fridgeventory.data.db.ItemEntity
import at.florianschmid.fridgeventory.data.db.ItemDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ItemRepository(private val itemDao: ItemDao) {

    fun getAllItems(): Flow<List<Item>> {
        return itemDao.getAllItems().map {
            it.map {item ->
                Item(
                    item._id,
                    item.name,
                    item.expiry_date,
                    item.quantity,
                    item.additional
                )
            }
        }
    }

    suspend fun findItemById(id: Int): Item {
        val item = itemDao.findItemById(id)
        return Item(
            item._id,
            item.name,
            item.expiry_date,
            item.quantity,
            item.additional
        )
    }


    suspend fun addItem(item:Item) {
        itemDao.addItem(ItemEntity(_id=0, item.name, item.expiry_date, item.quantity, item.additional))
    }

    suspend fun updateItem(item: Item) {
        itemDao.updateItem(ItemEntity(item.id, item.name, item.expiry_date, item.quantity, item.additional))
    }

    fun findItemsExpiringSoon(): Flow<List<Item>> {
        return itemDao.getItemsExpiringSoon().map {
            it.map {item ->
                Item(
                    item._id,
                    item.name,
                    item.expiry_date,
                    item.quantity,
                    item.additional
                )
            }
        }
    }
}
