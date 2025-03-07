package com.example.kinopoisk.presentation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Film(
    val id: Int? = null,
    val name: String? = null,
    val alternativeName: String? = null,
    val year: Int? = null,
    val description: String? = null,
    val rating: Rating? = null, // Сделайте nullable, если это возможно
    val poster: Poster? = null,
    val genres: List<Genre>? = null,
    val countries: List<Country>? = null,
    val imageUrl: String? = null,
    val votes: Votes? = null // Сделайте nullable, если это возможно
) : Parcelable


@Parcelize
data class Votes(
    val kp: Int?
): Parcelable

@Parcelize
data class Person(
    val id: Int,
    val name: String?,
    val photo: String? // Фото актера
): Parcelable

@Parcelize
data class Rating(
    val kp: Double,
    val imdb: Double,
    val tmdb: Double
) : Parcelable

@Parcelize
data class Poster(
    val url: String,
    val previewUrl: String? = null
) : Parcelable

@Parcelize
data class Genre(
    val name: String
) : Parcelable

@Parcelize
data class Country(
    val name: String
) : Parcelable
