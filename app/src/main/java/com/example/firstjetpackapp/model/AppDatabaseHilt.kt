package com.example.firstjetpackapp.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.firstjetpackapp.dao.UserDao

@Database(entities = [Users::class], version = 1)
abstract class AppDatabaseHilt : RoomDatabase() {

    // Just define the abstract function for your DAO
    abstract fun userDao(): UserDao

    // Notice how the ENTIRE companion object is completely gone!
    // Hilt's DatabaseModule is now responsible for providing this database.
}