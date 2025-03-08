package com.example.kinopoisk.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdditionalPageFilmViewModel : ViewModel() {

    // LiveData для данных
    private val _description = MutableLiveData<String>()
    val description: LiveData<String> get() = _description

    // Метод для установки описания
    fun setDescription(description: String) {
        _description.value = description
    }

    // Методы для навигации (без реализации)
    fun navigateToHome() {

    }

    fun navigateToAccount() {

    }

    fun navigateToFavorites() {

    }

    fun navigateBack() {

    }
}