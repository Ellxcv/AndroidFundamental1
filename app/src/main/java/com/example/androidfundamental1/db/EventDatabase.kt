package com.example.androidfundamental1.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.androidfundamental1.models.DetailEvent
import com.example.androidfundamental1.models.Events

@Database(entities = [DetailEvent::class, Events::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class EventDatabase : RoomDatabase() {

    abstract fun getEventDao(): DetailEventDAO

    companion object {
        @Volatile
        private var INSTANCE: EventDatabase? = null

        fun getDatabase(context: Context): EventDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java,
                    "event_database.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
