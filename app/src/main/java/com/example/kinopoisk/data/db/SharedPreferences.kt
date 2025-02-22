package com.example.kinopoisk.data.db

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUserEmail(email: String) {
        sharedPreferences.edit().putString("email", email).apply()
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString("email", null)
    }

    fun clearUserEmail() {
        sharedPreferences.edit().remove("email").apply()
    }
}