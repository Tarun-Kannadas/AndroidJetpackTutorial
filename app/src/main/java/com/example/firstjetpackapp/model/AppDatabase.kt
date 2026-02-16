package com.example.firstjetpackapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.firstjetpackapp.dao.UserDao

@Database(entities = [Users::class], version = 1)
// FIX 1: Must extend RoomDatabase()
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // If INSTANCE is not null, return it.
            // Otherwise, enter the synchronized block to create it.
            return INSTANCE ?: synchronized(this) {
                // FIX 2 & 3: Create the database and assign it properly
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user-db"
                ).build()

                INSTANCE = instance // Save it for future calls
                instance // Return the newly created instance
            }
        }
    }
}