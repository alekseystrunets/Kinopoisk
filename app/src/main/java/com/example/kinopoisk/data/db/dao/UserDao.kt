package com.example.kinopoisk.data.db.dao

import androidx.room.*
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
    @Insert(onConflict = OnConflictStrategy.IGNORE) // Игнорировать конфликт, если запись уже существует
    suspend fun insertFavorite(favorite: Favorites)

    @Query("SELECT * FROM favorites WHERE id = :filmId LIMIT 1")
    suspend fun getFavoriteById(filmId: Int): Favorites?

    @Delete
    suspend fun deleteFavorite(favorite: Favorites)

    // Методы для связи пользователей и избранных фильмов
    @Insert
    suspend fun insertUserFilm(userFilm: UserFilm)

    @Query("DELETE FROM user_films WHERE userEmail = :userEmail AND filmId = :filmId")
    suspend fun deleteUserFilm(userEmail: String, filmId: Int)

    @Query("SELECT favorites.* FROM favorites " +
            "INNER JOIN user_films ON favorites.id = user_films.filmId " +
            "WHERE user_films.userEmail = :userEmail")
    suspend fun getFavoritesForUser(userEmail: String): List<Favorites>

    @Query("SELECT * FROM favorites WHERE id = :filmId AND userEmail = :userEmail LIMIT 1")
    suspend fun getFavoriteByIdAndUser(filmId: Int, userEmail: String): Favorites?
}