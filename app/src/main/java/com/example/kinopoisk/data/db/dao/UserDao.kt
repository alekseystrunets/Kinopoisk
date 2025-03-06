package com.example.kinopoisk.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.kinopoisk.data.db.entity.Favorites
import com.example.kinopoisk.data.db.entity.User
import com.example.kinopoisk.data.db.entity.UserFilm

@Dao
interface UserDao {

    // Методы для пользователей
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getUserByEmailAndPassword(email: String, password: String): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    // Методы для избранных фильмов
    @Insert
    suspend fun insertFavorite(favorite: Favorites)

    @Query("SELECT * FROM favorites WHERE id = :filmId LIMIT 1")
    suspend fun getFavoriteById(filmId: Int): Favorites?

    // Методы для связи пользователей и избранных фильмов
    @Insert
    suspend fun insertUserFilm(userFilm: UserFilm)

    @Query("SELECT favorites.* FROM favorites " +
            "INNER JOIN user_films ON favorites.id = user_films.filmId " +
            "WHERE user_films.userEmail = :userEmail")
    suspend fun getFavoritesForUser(userEmail: String): List<Favorites>
}