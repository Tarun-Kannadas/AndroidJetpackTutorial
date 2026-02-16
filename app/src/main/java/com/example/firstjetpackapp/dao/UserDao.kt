package com.example.firstjetpackapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.firstjetpackapp.model.Users

@Dao
@JvmSuppressWildcards
interface  UserDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: Users): Long

    @Query("SELECT * FROM users where username=:username and password=:password")
    suspend fun getUsers(username: String, password: String): Users?
}