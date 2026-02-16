package com.example.firstjetpackapp.repository

import com.example.firstjetpackapp.dao.UserDao
import com.example.firstjetpackapp.model.Users

class AuthRepository(private val userDao: UserDao) {
    suspend fun registerUser(user: Users){
        userDao.insertUser(user)
    }

    suspend fun loginUser(username: String,password: String): Boolean
    {
        return userDao.getUsers(username,password) != null
    }
}