package com.example.kinopoisk.presentation.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.data.db.entity.User
import com.example.kinopoisk.data.db.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    application: Application,
    private val userRepository: UserRepository // Внедряем UserRepository через конструктор
) : AndroidViewModel(application) {

    // LiveData для данных пользователя
    private val _userData = MutableLiveData<User?>()
    val userData: LiveData<User?> get() = _userData

    // Получить email из SharedPreferences
    fun getUserEmail(): String? {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_email", null)
    }

    // Загрузить данные пользователя по email
    fun loadUserData(email: String) {
        viewModelScope.launch {
            val user = userRepository.getUserByEmail(email)
            _userData.value = user
        }
    }
}