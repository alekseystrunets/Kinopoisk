package com.example.kinopoisk.presentation.view_model

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.data.db.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class RegistrationFragmentViewModel @Inject constructor(
    application: Application,
    private val userRepository: UserRepository // Внедряем UserRepository через конструктор
) : AndroidViewModel(application) {

    // LiveData для ошибок валидации
    private val _liveDataForFields = MutableLiveData<Bundle?>()
    val publicLiveDataForFields: LiveData<Bundle?> get() = _liveDataForFields

    // LiveData для сообщений Toast
    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    private val EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
    private val LOGIN_PATTERN = Pattern.compile("^[A-Za-z0-9_]{3,20}\$")
    private val PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+\$).{8,}\$")

    // Валидация полей
    fun validateInputs(email: String, login: String, password: String): Boolean {
        val errors = Bundle()

        if (email.isEmpty()) {
            errors.putString("emailError", "Email cannot be empty")
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.putString("emailError", "Incorrect email format")
        }

        if (login.isEmpty()) {
            errors.putString("loginError", "Login cannot be empty")
        } else if (!LOGIN_PATTERN.matcher(login).matches()) {
            errors.putString("loginError", "The login must contain only letters, numbers and symbols (from 3 to 20 characters).")
        }

        if (password.isEmpty()) {
            errors.putString("passwordError", "Password cannot be empty")
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            errors.putString("passwordError", "Password must contain at least 8 characters, one number, one uppercase letter and one special character")
        }

        _liveDataForFields.value = if (errors.isEmpty) null else errors
        return errors.isEmpty
    }

    // Регистрация пользователя
    fun registerUser(email: String, login: String, password: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val success = userRepository.registerUser(email, login, password)
            if (success) {
                saveUserEmail(email)
                onSuccess(email)
                showToast("Registration successful!")
            } else {
                onError("A user with this email is already registered")
            }
        }
    }

    // Сохранение email в SharedPreferences
    private fun saveUserEmail(email: String) {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("user_email", email).apply()
        Log.d("RegistrationFragmentViewModel", "Email saved to SharedPreferences: $email")
    }

    // Управление сообщениями Toast
    private fun showToast(message: String) {
        _toastMessage.value = message
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}