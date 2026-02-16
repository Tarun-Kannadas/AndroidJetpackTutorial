package com.example.firstjetpackapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class Users(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Room will handle the ID generation for you
    val username: String,
    val password: String,
    val phnnumber: String,
    val email: String
)