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
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user-db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}