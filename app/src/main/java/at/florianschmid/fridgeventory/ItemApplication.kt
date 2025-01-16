package at.florianschmid.fridgeventory

import android.app.Application
import at.florianschmid.fridgeventory.data.ItemRepository
import at.florianschmid.fridgeventory.data.RecommendationRepository
import at.florianschmid.fridgeventory.data.db.ItemDatabase

class ItemApplication : Application() {

    val itemRepository by lazy {
        ItemRepository(
            ItemDatabase.getDatabase(this).itemDao()
        )
    }
    val recommendationRepository by lazy{
        RecommendationRepository(
            ItemDatabase.getDatabase(this).recommendationDao()
        )
    }

}
