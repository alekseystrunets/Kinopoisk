package com.example.kinopoisk.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorites(
    @PrimaryKey val id: Int,
    val name: String,
    val year: Int,
    val description: String,
    val posterUrl: String,
    val userEmail: String
)