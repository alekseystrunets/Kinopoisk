import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.data.db.AppDatabase
import com.example.kinopoisk.data.db.entity.User
import com.example.kinopoisk.data.db.repository.UserRepository
import kotlinx.coroutines.launch

class UserAccountViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository(AppDatabase.getDatabase(application).userDao())

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