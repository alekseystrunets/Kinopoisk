package com.example.kinopoisk.presentation.ui.fragment

import UserAccountFragment
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.kinopoisk.R
import com.example.kinopoisk.databinding.FragmentFilmPageBinding
import com.example.kinopoisk.presentation.Film

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