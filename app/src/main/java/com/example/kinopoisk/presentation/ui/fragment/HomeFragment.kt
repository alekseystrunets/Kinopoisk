import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinopoisk.R
import com.example.kinopoisk.data.api.RetrofitClient
import com.example.kinopoisk.databinding.FragmentHomeBinding
import com.example.kinopoisk.presentation.Film
import com.example.kinopoisk.presentation.adapter.CategoriesAdapter
import com.example.kinopoisk.presentation.interfaices.OnFilmClickListener
import com.example.kinopoisk.presentation.ui.fragment.FavoritesFragment
import com.example.kinopoisk.presentation.ui.fragment.FilmPageFragment
import com.example.kinopoisk.presentation.ui.fragment.UserAccountFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment(), OnFilmClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val apiKey = "API_KEY"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Устанавливаем пустой адаптер сразу
        binding.firstRecycleView.layoutManager = LinearLayoutManager(context)
        val adapter = CategoriesAdapter(mutableListOf(), this@HomeFragment)
        binding.firstRecycleView.adapter = adapter

        val categories = mutableListOf<Pair<String, List<Film>>>()

        lifecycleScope.launch {
            // Текущий год как строка
            val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()

            // Запускаем все запросы параллельно
            val topRatedDeferred = async {
                RetrofitClient.api.getMovies(
                    apiKey,
                    rating = "7-10", // Расширяем диапазон рейтинга
                    countries = "Россия,США,Франция,Великобритания", // Указываем несколько стран
                    limit = 3, // Увеличиваем лимит
                    page = 1
                )
            }
            val newMoviesDeferred = async {
                RetrofitClient.api.getMovies(
                    apiKey,
                    year = currentYear,
                    limit = 3 // Увеличиваем лимит
                )
            }
            val actionMoviesDeferred = async {
                RetrofitClient.api.getMovies(
                    apiKey,
                    genre = "боевик",
                    limit = 3 // Увеличиваем лимит
                )
            }
            val thrillersDeferred = async {
                RetrofitClient.api.getMovies(
                    apiKey,
                    genre = "триллер",
                    limit = 3 // Увеличиваем лимит
                )
            }
            val horrorsDeferred = async {
                RetrofitClient.api.getMovies(
                    apiKey,
                    genre = "ужасы",
                    limit = 3 // Увеличиваем лимит
                )
            }
            val topMoviesDeferred = async {
                RetrofitClient.api.getMovies(
                    apiKey,
                    rating = "6-10", // Расширяем диапазон рейтинга
                    limit = 3 // Увеличиваем лимит
                )
            }

            // Ожидаем завершения всех запросов
            val topRatedResponse = topRatedDeferred.await()
            val newMoviesResponse = newMoviesDeferred.await()
            val actionMoviesResponse = actionMoviesDeferred.await()
            val thrillersResponse = thrillersDeferred.await()
            val horrorsResponse = horrorsDeferred.await()
            val topMoviesResponse = topMoviesDeferred.await()

            // Логируем ответы
            Log.d("HomeFragment", "Ответ для Лучших фильмов: ${topRatedResponse.body()}")
            Log.d("HomeFragment", "Ответ для Новинок: ${newMoviesResponse.body()}")
            Log.d("HomeFragment", "Ответ для Боевиков: ${actionMoviesResponse.body()}")
            Log.d("HomeFragment", "Ответ для Триллеров: ${thrillersResponse.body()}")
            Log.d("HomeFragment", "Ответ для Хорроров: ${horrorsResponse.body()}")
            Log.d("HomeFragment", "Ответ для Топ фильмов: ${topMoviesResponse.body()}")

            // Обрабатываем ответы
            processResponse(topRatedResponse, "Лучшие фильмы", categories)
            processResponse(newMoviesResponse, "Новинки", categories)
            processResponse(actionMoviesResponse, "Боевики", categories)
            processResponse(thrillersResponse, "Триллеры", categories)
            processResponse(horrorsResponse, "Хорроры", categories)
            processResponse(topMoviesResponse, "Топ фильмы", categories)

            // Обновляем адаптер с новыми данными
            withContext(Dispatchers.Main) {
                adapter.updateData(categories)
                Log.d("HomeFragment", "Адаптер обновлён с ${categories.size} категориями")
            }
        }

        // Обработчики нажатий на кнопки
        binding.buttonAccount.setOnClickListener {
            toAccountScreen()
        }

        binding.buttonFavorites.setOnClickListener {
            toFavoritesScreen()
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
                    // Используем alternativeName, если name равен null
                    val title = apiFilm.name ?: apiFilm.alternativeName ?: return@mapNotNull null
                    val posterUrl = apiFilm.poster?.url ?: return@mapNotNull null
                    Film(title, posterUrl)
                }

                if (films.isNotEmpty()) {
                    categories.add(Pair(categoryName, films))
                    Log.d("HomeFragment", "Загружены $categoryName: ${films.size} шт.")
                } else {
                    Log.d("HomeFragment", "Список $categoryName пуст")
                }
            }
        } else {
            Log.e("HomeFragment", "Ошибка при загрузке $categoryName: ${response.errorBody()?.string()}")
        }
    }

    override fun onFilmClick(film: Film) {
        val filmPageFragment = FilmPageFragment.newInstance(film)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, filmPageFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun toAccountScreen() {
        val accountFragment = UserAccountFragment()
        parentFragmentManager.beginTransaction().replace(R.id.fragment_container, accountFragment)
            .addToBackStack(null).commit()
    }

    private fun toFavoritesScreen() {
        val favoritesFragment = FavoritesFragment()
        parentFragmentManager.beginTransaction().replace(R.id.fragment_container, favoritesFragment)
            .addToBackStack(null).commit()
    }
}