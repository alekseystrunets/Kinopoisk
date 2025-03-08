import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.data.api.RetrofitClient
import com.example.kinopoisk.presentation.Film
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.Calendar

class HomeViewModel : ViewModel() {

    private val apiKey = ""

    // LiveData для списка категорий фильмов
    private val _categories = MutableLiveData<List<Pair<String, List<Film>>>>()
    val categories: LiveData<List<Pair<String, List<Film>>>> get() = _categories

    // LiveData для ошибок
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    // Загрузка данных
    fun loadData() {
        viewModelScope.launch {
            val categories = mutableListOf<Pair<String, List<Film>>>()
            val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()

            try {
                val topRatedDeferred = async {
                    RetrofitClient.api.getMovies(
                        apiKey,
                        rating = "7-10",
                        countries = "Россия,США,Франция,Великобритания",
                        limit = 3,
                        page = 1
                    )
                }
                val newMoviesDeferred = async {
                    RetrofitClient.api.getMovies(
                        apiKey,
                        year = currentYear,
                        limit = 3
                    )
                }
                val actionMoviesDeferred = async {
                    RetrofitClient.api.getMovies(
                        apiKey,
                        genre = "боевик",
                        limit = 3
                    )
                }
                val thrillersDeferred = async {
                    RetrofitClient.api.getMovies(
                        apiKey,
                        genre = "триллер",
                        limit = 3
                    )
                }
                val horrorsDeferred = async {
                    RetrofitClient.api.getMovies(
                        apiKey,
                        genre = "ужасы",
                        limit = 3
                    )
                }
                val topMoviesDeferred = async {
                    RetrofitClient.api.getMovies(
                        apiKey,
                        rating = "6-10",
                        limit = 3
                    )
                }

                val topRatedResponse = topRatedDeferred.await()
                val newMoviesResponse = newMoviesDeferred.await()
                val actionMoviesResponse = actionMoviesDeferred.await()
                val thrillersResponse = thrillersDeferred.await()
                val horrorsResponse = horrorsDeferred.await()
                val topMoviesResponse = topMoviesDeferred.await()

                processResponse(topRatedResponse, "Лучшие фильмы", categories)
                processResponse(newMoviesResponse, "Новинки", categories)
                processResponse(actionMoviesResponse, "Боевики", categories)
                processResponse(thrillersResponse, "Триллеры", categories)
                processResponse(horrorsResponse, "Хорроры", categories)
                processResponse(topMoviesResponse, "Топ фильмы", categories)

                withContext(Dispatchers.Main) {
                    _categories.value = categories
                }
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка при загрузке данных: ${e.message}"
            }
        }
    }

    private fun processResponse(
        response: Response<ApiResponse>,
        categoryName: String,
        categories: MutableList<Pair<String, List<Film>>>
    ) {
        if (response.isSuccessful) {
            response.body()?.docs?.let { apiFilms ->
                val films = apiFilms.mapNotNull { apiFilm ->
                    val title = apiFilm.name ?: apiFilm.alternativeName ?: return@mapNotNull null
                    val posterUrl = apiFilm.poster?.url ?: return@mapNotNull null
                    apiFilm.copy(imageUrl = posterUrl)
                }

                if (films.isNotEmpty()) {
                    categories.add(Pair(categoryName, films))
                }
            }
        } else {
            Log.e("HomeViewModel", "Ошибка при загрузке $categoryName: ${response.errorBody()?.string()}")
        }
    }
}