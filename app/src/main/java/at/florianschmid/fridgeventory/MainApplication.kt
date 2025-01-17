package at.florianschmid.fridgeventory

import android.app.Application
import at.florianschmid.fridgeventory.data.ItemRepository
import at.florianschmid.fridgeventory.data.db.ItemDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

/*    val itemRepository by lazy {
        ItemRepository(
            ItemDatabase.getDatabase(this).itemDao()
        )
    }
    val recommendationRepository by lazy{
        RecommendationRepository(
            ItemDatabase.getDatabase(this).recommendationDao()
        )
    }*/

}
