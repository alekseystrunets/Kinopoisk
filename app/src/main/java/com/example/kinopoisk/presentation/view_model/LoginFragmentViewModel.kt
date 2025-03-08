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
class LoginFragmentViewModel @Inject constructor(
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
    private val PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+\$).{8,}\$")

    // Валидация полей
    fun validateInputs(email: String, password: String): Boolean {
        val errors = Bundle()

        if (email.isEmpty()) {
            errors.putString("emailError", "Email cannot be empty")
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.putString("emailError", "Incorrect email format")
        }

        if (password.isEmpty()) {
            errors.putString("passwordError", "Password cannot be empty")
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            errors.putString("passwordError", "Password must contain at least 8 characters, one number, one uppercase letter and one special character")
        }

        _liveDataForFields.value = if (errors.isEmpty) null else errors
        return errors.isEmpty
    }

    // Логин пользователя
    fun loginUser(email: String, password: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val success = userRepository.loginUser(email, password)
            if (success) {
                saveUserEmail(email)
                onSuccess(email)
                showToast("Login successful!")
            } else {
                onError("Incorrect email or password")
            }
        }
    }

    // Сохранение email в SharedPreferences
    private fun saveUserEmail(email: String) {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("user_email", email).apply()
        Log.d("LoginFragmentViewModel", "Email saved to SharedPreferences: $email")
    }

    // Управление сообщениями Toast
    private fun showToast(message: String) {
        _toastMessage.value = message
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}