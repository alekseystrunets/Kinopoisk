package com.example.kinopoisk.data.model

import com.google.gson.annotations.SerializedName

data class PersonResponse(
    @SerializedName("name") val name: String, // Имя актера
    @SerializedName("photo") val photo: String // URL фотографии
)