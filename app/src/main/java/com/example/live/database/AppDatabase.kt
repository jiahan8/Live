package com.example.live.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.live.database.dao.PostDao
import com.example.live.database.model.DatabasePost

private const val DATABASE_NAME = "post-database"

/**
 * The Room database for this app
 */
@Database(entities = [DatabasePost::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
        }
    }
}