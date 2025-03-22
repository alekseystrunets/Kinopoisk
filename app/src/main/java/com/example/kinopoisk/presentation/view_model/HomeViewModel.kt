import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.BuildConfig
import com.example.kinopoisk.data.api.RetrofitClient
import com.example.kinopoisk.presentation.Film
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.Calendar

class HomeViewModel : ViewModel() {

    private val apiKey = BuildConfig.KINOPOISK_API_KEY

    // LiveData для списка категорий фильмов
    private val _categories = MutableLiveData<Map<String, List<Film>>>()
    val categories: LiveData<Map<String, List<Film>>> get() = _categories

    // LiveData для ошибок
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    // Загрузка данных
    fun loadData() {
        viewModelScope.launch {
            val categories = mutableMapOf<String, List<Film>>()
            val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()

            try {
                val requests = listOf(
                    "Best films" to async(Dispatchers.IO) {
                        RetrofitClient.api.getMovies(
                            apiKey,
                            rating = "7-10",
                            countries = "Россия,США,Франция,Великобритания",
                            limit = 5,
                            page = 1
                        )
                    },
                    "New movies" to async(Dispatchers.IO) {
                        RetrofitClient.api.getMovies(
                            apiKey,
                            year = currentYear,
                            limit = 5
                        )
                    },
                    "Action" to async(Dispatchers.IO) {
                        RetrofitClient.api.getMovies(
                            apiKey,
                            genre = "боевик",
                            limit = 5
                        )
                    },
                    "Thrillers" to async(Dispatchers.IO) {
                        RetrofitClient.api.getMovies(
                            apiKey,
                            genre = "триллер",
                            limit = 5
                        )
                    },
                    "Horror" to async(Dispatchers.IO) {
                        RetrofitClient.api.getMovies(
                            apiKey,
                            genre = "ужасы",
                            limit = 5
                        )
                    },
                    "Top movies" to async(Dispatchers.IO) {
                        RetrofitClient.api.getMovies(
                            apiKey,
                            rating = "6-10",
                            limit = 5
                        )
                    }
                )

                // Обрабатываем результаты
                requests.forEach { (categoryName, deferredResponse) ->
                    val response = deferredResponse.await()
                    processResponse(response, categoryName, categories)
                }

                // Обновляем LiveData в основном потоке
                withContext(Dispatchers.Main) {
                    _categories.value = categories
                }
            } catch (e: Exception) {
                // Обрабатываем ошибки
                _errorMessage.value = "Error loading data: ${e.message}"
            }
        }
    }

    // Обработка ответа от API
    private fun processResponse(
        response: Response<ApiResponse>,
        categoryName: String,
        categories: MutableMap<String, List<Film>>
    ) {
        if (response.isSuccessful) {
            response.body()?.docs?.let { apiFilms ->
                val films = apiFilms.mapNotNull { apiFilm ->
                    val posterUrl = apiFilm.poster?.url ?: return@mapNotNull null
                    apiFilm.copy(imageUrl = posterUrl)
                }

                if (films.isNotEmpty()) {
                    categories[categoryName] = films
                }
            }
        } else {
            Log.e("HomeViewModel", "Ошибка при загрузке $categoryName: ${response.errorBody()?.string()}")
        }
    }
}