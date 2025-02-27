package com.example.kinopoisk.presentation.ui.fragment

import ApiResponse
import UserAccountFragment
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.Calendar

class HomeFragment : Fragment(), OnFilmClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val apiKey = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.firstRecycleView.layoutManager = LinearLayoutManager(context)
        val adapter = CategoriesAdapter(mutableListOf(), this@HomeFragment)
        binding.firstRecycleView.adapter = adapter

        val categories = mutableListOf<Pair<String, List<Film>>>()

        lifecycleScope.launch {
            val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()

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
                adapter.updateData(categories)
                Log.d("HomeFragment", "Адаптер обновлён с ${categories.size} категориями")
            }
        }

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
                    val title = apiFilm.name ?: apiFilm.alternativeName ?: return@mapNotNull null
                    val posterUrl = apiFilm.poster?.url ?: return@mapNotNull null
                    apiFilm.copy(imageUrl = posterUrl)
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
