package com.example.firstjetpackapp.repository

import com.example.firstjetpackapp.dao.UserDao
import com.example.firstjetpackapp.model.Users
import javax.inject.Inject

class AuthRepositoryHilt @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun registerUserHilt(user: Users){
        userDao.insertUser(user)
    }

    suspend fun loginUserHilt(username: String,password: String): Boolean {
        return userDao.getUsers(username,password) != null
    }
}