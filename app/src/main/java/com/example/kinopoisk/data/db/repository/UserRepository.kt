package com.example.kinopoisk.data.db.repository

import com.example.kinopoisk.data.db.dao.UserDao
import com.example.kinopoisk.data.db.entity.Favorites
import com.example.kinopoisk.data.db.entity.User
import com.example.kinopoisk.data.db.entity.UserFilm
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {

    // Методы для пользователей
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

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    // Методы для избранных фильмов
    suspend fun addFavoriteToUser(userEmail: String, favorite: Favorites) {
        userDao.insertFavorite(favorite)
        val userFilm = UserFilm(userEmail = userEmail, filmId = favorite.id)
        userDao.insertUserFilm(userFilm)
    }

    suspend fun getFavoritesForUser(userEmail: String): List<Favorites> {
        return userDao.getFavoritesForUser(userEmail)
    }

    // Метод для удаления фильма из избранного
    suspend fun deleteFavorite(userEmail: String, favorite: Favorites) {
        userDao.deleteFavorite(favorite)
        userDao.deleteUserFilm(userEmail, favorite.id)
    }
}