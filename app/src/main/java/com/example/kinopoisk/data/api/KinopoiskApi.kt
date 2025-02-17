package com.example.kinopoisk.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface KinopoiskApi {
    @GET("actors")
    suspend fun getActors(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): List<ActorsResponse>
}

data class ActorsResponse(
    val name: String,
    val imageUrl: String
)