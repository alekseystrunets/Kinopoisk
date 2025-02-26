import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.data.db.AppDatabase
import com.example.kinopoisk.data.db.repository.UserRepository
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class RegistrationFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val _liveDataForFields = MutableLiveData<Bundle?>()
    val publicLiveDataForFields: LiveData<Bundle?> get() = _liveDataForFields

    private val database = AppDatabase.getDatabase(application)
    private val userDao = database.userDao()
    private val userRepository = UserRepository(userDao)

    private val EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
    private val LOGIN_PATTERN = Pattern.compile("^[A-Za-z0-9_]{3,20}\$")
    private val PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+\$).{8,}\$")

    fun validateInputs(email: String, login: String, password: String) {
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
    }

    fun registerUser(email: String, login: String, password: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val success = userRepository.registerUser(email, login, password)
            if (success) {
                onSuccess(email)
            } else {
                onError("Пользователь с таким email уже зарегистрирован")
            }
        }
    }
}