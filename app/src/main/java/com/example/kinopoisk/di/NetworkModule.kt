package com.example.kinopoisk.di

import KinopoiskApi
import com.example.kinopoisk.data.api.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//object NetworkModule {
//
//    @Provides
//    @Singleton
//    fun provideRetrofit(): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl("https://api.kinopoisk.dev/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideKinopoiskApi(retrofit: Retrofit): KinopoiskApi {
//        return retrofit.create(KinopoiskApi::class.java)
//    }
//}