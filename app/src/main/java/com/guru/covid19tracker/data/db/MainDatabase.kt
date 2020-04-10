package com.guru.covid19tracker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.guru.covid19tracker.models.ApiData
import com.guru.covid19tracker.utils.ListTypeConverter

@Database(
    version = 2,
    entities = [ApiData::class]
)
@TypeConverters(ListTypeConverter::class)
abstract class MainDatabase: RoomDatabase() {
    // We are not using api set anymore as api for covid was very unstable so we moved to firebase for api modification and providing data
    // Hence Firebase already has local cache so we may use room for other purposes.
    abstract fun mainDao(): MainDao

    companion object {
        private const val DATABASE_NAME = "main.db"
        private var instance: MainDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context.applicationContext).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MainDatabase::class.java, DATABASE_NAME
            ).build()
    }
}