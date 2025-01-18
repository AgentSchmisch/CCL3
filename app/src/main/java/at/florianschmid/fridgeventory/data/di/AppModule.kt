package at.florianschmid.fridgeventory.data.di

import android.content.Context
import androidx.room.Room
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import at.florianschmid.fridgeventory.ExpiringItems.Recipes.RecipeService
import at.florianschmid.fridgeventory.ExpiryNotificationWorker
import at.florianschmid.fridgeventory.data.ItemRepository
import at.florianschmid.fridgeventory.data.db.ItemDao
import at.florianschmid.fridgeventory.data.db.ItemDatabase
import at.florianschmid.fridgeventory.data.db.RecommendationDao
import at.florianschmid.fridgeventory.data.remote.RemoteService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext:Context): ItemDatabase {
        return Room.databaseBuilder(appContext, ItemDatabase::class.java, "fridgeventory_database")
            .fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    @Singleton
    fun provideItemRepository(itemDao: ItemDao): ItemRepository{
        return ItemRepository(itemDao)
    }

    @Provides
    @Singleton
    fun provideItemDao(database: ItemDatabase): ItemDao {
        return database.itemDao()
    }

    @Provides
    @Singleton
    fun provideWorkerManager(@ApplicationContext appContext:Context): WorkManager {
        return WorkManager.getInstance(appContext)
    }


    @Provides
    @Singleton
    fun provideWorkerParams(): WorkRequest {
        return  PeriodicWorkRequest.Builder(
            ExpiryNotificationWorker::class.java,
            12, // Interval duration
            TimeUnit.HOURS // Interval time unit
        ).build()
    }

    @Provides
    @Singleton
    fun provideRecipeService(): RecipeService {
        return RecipeService()
    }

    @Provides
    @Singleton
    fun provideRemoteService() : RemoteService {
        return RemoteService()
    }
}