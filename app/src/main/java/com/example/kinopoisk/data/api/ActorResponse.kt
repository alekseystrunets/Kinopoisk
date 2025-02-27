package com.example.kinopoisk.presentation

data class ActorsResponse(
    val docs: List<Actor>, // Список актеров
    val total: Int,
    val limit: Int,
    val page: Int,
    val pages: Int
)

data class Actor(
    val id: Int,
    val name: String,
    val photo: String?
)