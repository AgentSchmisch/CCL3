package at.florianschmid.fridgeventory.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ItemEntity::class, RecommendationEntity::class], version = 4)
@TypeConverters(Converters::class) // add the date converters to the database
abstract class ItemDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun recommendationDao(): RecommendationDao
}