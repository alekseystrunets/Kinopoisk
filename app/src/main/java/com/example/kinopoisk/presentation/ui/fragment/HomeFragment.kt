package com.example.kinopoisk.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kinopoisk.R
import com.example.kinopoisk.databinding.FragmentHomeBinding
import com.example.kinopoisk.presentation.Film
import com.example.kinopoisk.presentation.adapter.CategoriesAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categories = listOf(
            Pair("Лучшие фильмы", listOf(
                Film("Три мушкетера", "image_url_1"),
                Film("Фильм 2", "image_url_2"),
                Film("Три мушкетера", "image_url_1"),
                Film("Три мушкетера", "image_url_1"),
                Film("Фильм 2", "image_url_2"),
                Film("Три мушкетера", "image_url_1"),
                Film("Фильм 2", "image_url_2")
            )),
            Pair("Новинки", listOf(
                Film("Фильм 3", "image_url_3"),
                Film("Фильм 3", "image_url_3"),
                Film("Фильм 3", "image_url_3"),
                Film("Фильм 3", "image_url_3"),
                Film("Фильм 3", "image_url_3"),
                Film("Фильм 3", "image_url_3"),
                Film("Фильм 4", "image_url_4")
            )),
                    Pair("Топ", listOf(
                Film("Фильм 3", "image_url_3"),
                Film("Фильм 3", "image_url_3"),
                Film("Фильм 3", "image_url_3"),
                Film("Фильм 3", "image_url_3"),
                Film("Фильм 3", "image_url_3"),
                Film("Фильм 3", "image_url_3"),
                Film("Фильм 4", "image_url_4")
            )),
        Pair("Актуальное", listOf(
            Film("Фильм 3", "image_url_3"),
            Film("Фильм 3", "image_url_3"),
            Film("Фильм 3", "image_url_3"),
            Film("Фильм 3", "image_url_3"),
            Film("Фильм 3", "image_url_3"),
            Film("Фильм 3", "image_url_3"),
            Film("Фильм 4", "image_url_4")
        )),
        Pair("Сериалы", listOf(
            Film("Фильм 3", "image_url_3"),
            Film("Фильм 3", "image_url_3"),
            Film("Фильм 3", "image_url_3"),
            Film("Фильм 3", "image_url_3"),
            Film("Фильм 3", "image_url_3"),
            Film("Фильм 3", "image_url_3"),
            Film("Фильм 4", "image_url_4")
        ))
        )

        binding.firstRecycleView.layoutManager = LinearLayoutManager(context)
        binding.firstRecycleView.adapter = CategoriesAdapter(categories)

        binding.buttonAccount.setOnClickListener {
            toAccountScreen()
        }

        binding.buttonFavorites.setOnClickListener {
            toFavoritesScreen()
        }
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
