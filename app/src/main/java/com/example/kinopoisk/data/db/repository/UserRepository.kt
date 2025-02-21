package com.example.kinopoisk.data.db.repository

import com.example.kinopoisk.data.db.dao.UserDao
import com.example.kinopoisk.data.db.entity.User

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(email: String, login: String, password: String): Boolean {
        val existingUser = userDao.getUserByEmail(email)
        return if (existingUser == null) {
            val user = User(email = email, login = login, password = password)
            userDao.insertUser(user)
            true
        } else {
            false
        }
    }

    suspend fun loginUser(email: String, password: String): Boolean {
        val user = userDao.getUserByEmailAndPassword(email, password)
        return user != null
    }

    suspend fun isFirstLogin(email: String): Boolean {
        return userDao.getUserByEmail(email) == null
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    suspend fun getUserById(id: Int): User? {
        return userDao.getUserById(id)
    }
}