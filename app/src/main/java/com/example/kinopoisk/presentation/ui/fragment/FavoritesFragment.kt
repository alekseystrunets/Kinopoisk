package com.example.kinopoisk.presentation.ui.fragment

import UserAccountFragment
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinopoisk.R
import com.example.kinopoisk.databinding.FragmentFavoritesBinding
import com.example.kinopoisk.presentation.adapter.FavoritesFragmentAdapter
import com.example.kinopoisk.data.db.AppDatabase
import com.example.kinopoisk.presentation.Film
import com.example.kinopoisk.presentation.Poster
import com.example.kinopoisk.presentation.Rating
import com.example.kinopoisk.presentation.Votes
import com.example.kinopoisk.presentation.Country
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private var recyclerView: RecyclerView? = null
    private var adapter: FavoritesFragmentAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация RecyclerView
        recyclerView = binding.recyclerView
        recyclerView?.layoutManager = LinearLayoutManager(context)

        // Получение избранных фильмов из базы данных
        loadFavorites()

        // Обработчики нажатий на кнопки
        binding.buttonHome.setOnClickListener {
            toHomeScreen()
        }

        binding.buttonAccount.setOnClickListener {
            toAccountScreen()
        }
    }

    private fun loadFavorites() {
        // Получаем email текущего пользователя из SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("user_email", null)

        if (userEmail != null) {
            // Загружаем избранные фильмы из базы данных
            CoroutineScope(Dispatchers.IO).launch {
                val database = AppDatabase.getDatabase(requireContext())
                val favoritesFromDb = database.userDao().getFavoritesForUser(userEmail)

                // Обновляем UI на главном потоке
                activity?.runOnUiThread {
                    if (favoritesFromDb.isNotEmpty()) {
                        // Инициализируем адаптер и передаем данные
                        adapter = FavoritesFragmentAdapter(favoritesFromDb) { favorite ->
                            // Создаем объект Film из Favorites
                            val film = Film(
                                id = favorite.id,
                                name = favorite.name,
                                year = favorite.year,
                                description = favorite.description,
                                poster = Poster(favorite.posterUrl, null),
                                rating = Rating(
                                    kp = favorite.rating ?: 0.0, // Значение по умолчанию, если rating отсутствует
                                    imdb = 0.0, // Значение по умолчанию для imdb
                                    tmdb = 0.0  // Значение по умолчанию для tmdb
                                ),
                                votes = Votes(favorite.votes ?: 0), // Значение по умолчанию, если votes отсутствует
                                countries = listOf(Country("Unknown"))
                            )
                            // Переход на FilmPageFragment
                            val filmPageFragment = FilmPageFragment.newInstance(film)
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, filmPageFragment)
                                .addToBackStack(null)
                                .commit()
                        }
                        recyclerView?.adapter = adapter
                    } else {
                        Toast.makeText(requireContext(), "Нет избранных фильмов", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(requireContext(), "Пользователь не авторизован", Toast.LENGTH_SHORT).show()
        }
    }

    private fun toHomeScreen() {
        val homeScreen = HomeFragment()
        parentFragmentManager.beginTransaction().replace(R.id.fragment_container, homeScreen)
            .addToBackStack(null).commit()
    }

    private fun toAccountScreen() {
        val accountFragment = UserAccountFragment()
        parentFragmentManager.beginTransaction().replace(R.id.fragment_container, accountFragment)
            .addToBackStack(null).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}