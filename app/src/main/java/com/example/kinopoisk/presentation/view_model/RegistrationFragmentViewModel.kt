import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.data.db.AppDatabase
import com.example.kinopoisk.data.db.repository.UserRepository
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class RegistrationFragmentViewModel(application: Application) : AndroidViewModel(application) {

    // LiveData для ошибок валидации
    private val _liveDataForFields = MutableLiveData<Bundle?>()
    val publicLiveDataForFields: LiveData<Bundle?> get() = _liveDataForFields

    // LiveData для сообщений Toast
    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    private val database = AppDatabase.getDatabase(application)
    private val userDao = database.userDao()
    private val userRepository = UserRepository(userDao)

    private val EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
    private val LOGIN_PATTERN = Pattern.compile("^[A-Za-z0-9_]{3,20}\$")
    private val PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+\$).{8,}\$")

    // Валидация полей
    fun validateInputs(email: String, login: String, password: String): Boolean {
        val errors = Bundle()

        if (email.isEmpty()) {
            errors.putString("emailError", "Email не может быть пустым")
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.putString("emailError", "Некорректный формат email")
        }

        if (login.isEmpty()) {
            errors.putString("loginError", "Логин не может быть пустым")
        } else if (!LOGIN_PATTERN.matcher(login).matches()) {
            errors.putString("loginError", "Логин должен содержать только буквы, цифры и символы подчеркивания (от 3 до 20 символов)")
        }

        if (password.isEmpty()) {
            errors.putString("passwordError", "Пароль не может быть пустым")
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            errors.putString("passwordError", "Пароль должен содержать минимум 8 символов, одну цифру, одну заглавную букву и один специальный символ")
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
                onError("Пользователь с таким email уже зарегистрирован")
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