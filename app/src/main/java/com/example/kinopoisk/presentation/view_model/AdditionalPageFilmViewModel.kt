package com.example.kinopoisk.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kinopoisk.data.db.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdditionalPageFilmViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // LiveData для данных
    private val _description = MutableLiveData<String>()
    val description: LiveData<String> get() = _description

    // Метод для установки описания
    fun setDescription(description: String) {
        _description.value = description
    }

    // Пример использования репозитория
    suspend fun registerUser(email: String, login: String, password: String): Boolean {
        return userRepository.registerUser(email, login, password)
    }

    // Методы для навигации (без реализации)
    fun navigateToHome() {}

    fun navigateToAccount() {}

    fun navigateToFavorites() {}

    fun navigateBack() {}
}