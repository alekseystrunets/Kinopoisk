package com.example.kinopoisk.presentation.ui.fragment

import UserAccountFragment
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinopoisk.R
import com.example.kinopoisk.databinding.FragmentFilmPageBinding
import com.example.kinopoisk.presentation.Actors
import com.example.kinopoisk.presentation.Film
import com.example.kinopoisk.presentation.adapter.FilmPageFragmentAdapter

class FilmPageFragment : Fragment() {

    private var _binding: FragmentFilmPageBinding? = null
    private val binding get() = _binding!!

    private var recyclerView: RecyclerView? = null
    private var adapter: FilmPageFragmentAdapter? = null

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

        // Настройка RecyclerView
        recyclerView = binding.recVwActors
        adapter = FilmPageFragmentAdapter(mutableListOf())
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Отображение данных о фильме
        film?.let { film ->
            Log.d("FilmPageFragment", "Persons: ${film.persons}") // Логируем список актеров

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

            // Загрузка актеров из поля `persons`
            val actors = film.persons?.mapNotNull { person ->
                Actors(
                    id = person.id ?: 0,
                    name = person.name ?: "Unknown",
                    photo = person.photo ?: ""
                )
            }?.take(4)

            Log.d("FilmPageFragment", "Actors: $actors") // Логируем список актеров

            if (actors != null) {
                adapter?.updateActors(actors)
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
        val additionalPageFilmFragment = AdditionalPageFilmFragment()
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