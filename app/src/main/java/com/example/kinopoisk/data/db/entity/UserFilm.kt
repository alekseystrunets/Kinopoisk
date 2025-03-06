package com.example.kinopoisk.data.db.entity

import androidx.room.Entity

@Entity(tableName = "user_films", primaryKeys = ["userEmail", "filmId"])
data class UserFilm(
    val userEmail: String,
    val filmId: Int
)