package com.example.kinopoisk.presentation.fragments

import HomeFragment
import UserAccountFragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinopoisk.R
import com.example.kinopoisk.data.db.entity.Favorites
import com.example.kinopoisk.databinding.FragmentFavoritesBinding
import com.example.kinopoisk.presentation.Film
import com.example.kinopoisk.presentation.Poster
import com.example.kinopoisk.presentation.Rating
import com.example.kinopoisk.presentation.Votes
import com.example.kinopoisk.presentation.Country
import com.example.kinopoisk.presentation.adapter.FavoritesFragmentAdapter
import com.example.kinopoisk.presentation.viewmodel.FavoritesViewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavoritesViewModel

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

        viewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)

        // Инициализация RecyclerView
        recyclerView = binding.recyclerView
        recyclerView?.layoutManager = LinearLayoutManager(context)

        // Получение email текущего пользователя из SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("user_email", null)

        if (userEmail != null) {
            // Загрузка избранных фильмов
            viewModel.loadFavorites(userEmail)
        } else {
            Toast.makeText(requireContext(), getString(R.string.not_authorize), Toast.LENGTH_SHORT).show()
        }

        // Подписываемся на данные о избранных фильмах
        viewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            if (favorites.isNotEmpty()) {
                // Инициализируем адаптер и передаем данные
                adapter = FavoritesFragmentAdapter(
                    favorites = favorites,
                    onItemClick = { favorite ->
                        // Создаем объект Film из Favorites
                        val film = Film(
                            id = favorite.id,
                            name = favorite.name,
                            year = favorite.year,
                            description = favorite.description,
                            poster = Poster(favorite.posterUrl, null),
                            rating = Rating(
                                kp = favorite.rating ?: 0.0,
                                imdb = 0.0,
                                tmdb = 0.0
                            ),
                            votes = Votes(favorite.votes ?: 0),
                            countries = listOf(Country("Unknown"))
                        )
                        // Переход на FilmPageFragment
                        val filmPageFragment = FilmPageFragment.newInstance(film)
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, filmPageFragment)
                            .addToBackStack(null)
                            .commit()
                    },
                    onMenuItemClick = { favorite, menuItemId ->
                        when (menuItemId) {
                            R.id.action_delete -> {
                                // Удаление фильма из избранного
                                if (userEmail != null) {
                                    viewModel.deleteFavorite(userEmail, favorite)
                                    // Обновляем список в адаптере
                                    adapter?.removeItem(adapter?.getPosition(favorite) ?: -1)
                                }
                            }
                            R.id.action_share -> {
                                // Шаринг информации о фильме
                                shareFilmInfo(favorite)
                            }
                        }
                    }
                )
                recyclerView?.adapter = adapter
            } else {
                Toast.makeText(requireContext(), getString(R.string.no_featured_films), Toast.LENGTH_SHORT).show()
            }
        }

        // Подписываемся на сообщения Toast
        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        // Обработчики нажатий на кнопки
        binding.buttonHome.setOnClickListener {
            toHomeScreen()
        }

        binding.buttonAccount.setOnClickListener {
            toAccountScreen()
        }
    }

    private fun shareFilmInfo(favorite: Favorites) {
        // Формируем текст для шаринга
        val shareText = """
            Название: ${favorite.name}
            Год: ${favorite.year}
            Описание: ${favorite.description}
        """.trimIndent()

        // Создаем Intent для шаринга
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }

        // Запускаем Intent
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_movie)))
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