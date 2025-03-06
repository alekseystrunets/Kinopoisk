package com.example.kinopoisk.presentation.ui.fragment

import UserAccountFragment
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.kinopoisk.R
import com.example.kinopoisk.databinding.FragmentFilmPageBinding
import com.example.kinopoisk.presentation.Film
import com.example.kinopoisk.data.db.AppDatabase
import com.example.kinopoisk.data.db.entity.Favorites
import com.example.kinopoisk.data.db.entity.UserFilm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FilmPageFragment : Fragment() {

    private var _binding: FragmentFilmPageBinding? = null
    private val binding get() = _binding!!

    private var film: Film? = null

    companion object {
        private const val ARG_FILM = "film"

        // Метод для создания нового экземпляра фрагмента с передачей данных о фильме
        fun newInstance(film: Film): FilmPageFragment {
            val args = Bundle().apply {
                putParcelable(ARG_FILM, film)
            }
            val fragment = FilmPageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Получаем данные о фильме из аргументов
        arguments?.let {
            film = it.getParcelable(ARG_FILM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Инициализация ViewBinding
        _binding = FragmentFilmPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Отображение данных о фильме
        film?.let { film ->

            // Название фильма
            binding.nameOfTheFilm.text = film.name ?: film.alternativeName ?: "Unknown"
            // Информация о фильме (год и страны)
            binding.informationAboutFilm.text = "${film.year ?: "Unknown"}, ${film.countries?.joinToString { country -> country.name } ?: "Unknown"}"
            // Описание фильма
            binding.filmDescription.text = film.description ?: "No description available"

            // Загрузка постера фильма с помощью Glide
            Glide.with(this)
                .load(film.poster?.url)
                .into(binding.imgFromApi)

            // Отображение оценки и количества оценок
            film.rating?.kp?.let { rating ->
                // Форматируем оценку до одного знака после запятой
                val formattedRating = String.format("%.1f", rating)
                binding.textViewRating.text = formattedRating

                // Изменение цвета текста в зависимости от оценки
                val textColor = if (rating <= 6.5) {
                    ContextCompat.getColor(requireContext(), R.color.red) // Красный цвет
                } else {
                    ContextCompat.getColor(requireContext(), R.color.green) // Зеленый цвет
                }
                binding.textViewRating.setTextColor(textColor)
            }

            // Отображение количества оценок
            film.votes?.kp?.let { votes ->
                binding.allCountsOfRating.text = "$votes оценок"
            }
        }

        // Обработчики нажатий на кнопки
        binding.descriptiomAboutFilm.setOnClickListener {
            toAdditionalPageFilm()
        }

        binding.buttonHome.setOnClickListener {
            toHomeScreen()
        }

        binding.buttonAccount.setOnClickListener {
            toAccountScreen()
        }

        binding.buttonFavorites.setOnClickListener {
            toFavoritesScreen()
        }

        // Обработчик нажатия на кнопку "Добавить в избранное"
        binding.buttonAddToFavorites.setOnClickListener {
            film?.let { film ->
                // Получаем email текущего пользователя из SharedPreferences
                val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                val userEmail = sharedPreferences.getString("user_email", null)

                if (userEmail != null) {
                    // Создаем объект Favorites
                    val favorite = Favorites(
                        id = film.id ?: 0, // Убедитесь, что film.id не null
                        name = film.name ?: "Unknown",
                        year = film.year ?: 0,
                        description = film.description ?: "No description",
                        posterUrl = film.poster?.url ?: "",
                        userEmail = userEmail // Используем email пользователя
                    )

                    // Сохраняем фильм в базу данных
                    CoroutineScope(Dispatchers.IO).launch {
                        val database = AppDatabase.getDatabase(requireContext())
                        database.userDao().insertFavorite(favorite)

                        // Создаем связь между пользователем и фильмом
                        val userFilm = UserFilm(
                            userEmail = userEmail,
                            filmId = favorite.id
                        )
                        database.userDao().insertUserFilm(userFilm)
                    }

                    Toast.makeText(requireContext(), "Фильм добавлен в избранное", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Пользователь не авторизован", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Методы для навигации между фрагментами
    private fun toHomeScreen() {
        val homeScreen = HomeFragment()
        parentFragmentManager.beginTransaction().replace(R.id.fragment_container, homeScreen)
            .addToBackStack(null).commit()
    }

    private fun toAdditionalPageFilm() {
        val additionalPageFilmFragment = AdditionalPageFilmFragment.newInstance(film?.description ?: "No description available")
        parentFragmentManager.beginTransaction().replace(R.id.fragment_container, additionalPageFilmFragment)
            .addToBackStack(null).commit()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}