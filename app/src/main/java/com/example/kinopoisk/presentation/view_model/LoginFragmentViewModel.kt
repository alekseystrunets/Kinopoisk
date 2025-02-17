package com.example.kinopoisk.presentation.view_model

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.regex.Pattern

class LoginFragmentViewModel : ViewModel() {

    private val _liveDataForFields: MutableLiveData<Bundle?> = MutableLiveData()
    val publicLiveDataForFields: LiveData<Bundle?> get() = _liveDataForFields

    // Регулярные выражения для валидации
    private val EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
    )
    private val LOGIN_PATTERN = Pattern.compile(
        "^[A-Za-z0-9_]{3,20}\$"
    )
    private val PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}\$"
    )

    fun validateInputs(email: String, login: String, password: String) {
        val errors = Bundle()

        // Валидация email
        if (email.isEmpty()) {
            errors.putString("emailError", "Email не может быть пустым")
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.putString("emailError", "Некорректный формат email")
        }

        // Валидация логина
        if (login.isEmpty()) {
            errors.putString("loginError", "Логин не может быть пустым")
        } else if (!LOGIN_PATTERN.matcher(login).matches()) {
            errors.putString(
                "loginError",
                "Логин должен содержать только буквы, цифры и символы подчеркивания (от 3 до 20 символов)"
            )
        }

        // Валидация пароля
        if (password.isEmpty()) {
            errors.putString("passwordError", "Пароль не может быть пустым")
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            errors.putString(
                "passwordError",
                "Пароль должен содержать минимум 8 символов, одну цифру, одну заглавную букву и один специальный символ"
            )
        }

        // Если ошибок нет, очищаем Bundle
        if (errors.isEmpty) {
            _liveDataForFields.value = null
        } else {
            _liveDataForFields.value = errors
        }
    }
}