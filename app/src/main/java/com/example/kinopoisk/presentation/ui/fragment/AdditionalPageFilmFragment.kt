package com.example.kinopoisk.presentation.ui.fragment

import UserAccountFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kinopoisk.R
import com.example.kinopoisk.databinding.FragmentAdditionalPageFilmBinding

class AdditionalPageFilmFragment : Fragment() {

    private var _binding: FragmentAdditionalPageFilmBinding? = null
    private val binding get() = _binding!!

    // Аргумент для передачи текста
    companion object {
        private const val ARG_DESCRIPTION = "description"

        // Метод для создания нового экземпляра фрагмента с передачей данных
        fun newInstance(description: String): AdditionalPageFilmFragment {
            val args = Bundle().apply {
                putString(ARG_DESCRIPTION, description)
            }
            val fragment = AdditionalPageFilmFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdditionalPageFilmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем текст описания из аргументов
        val description = arguments?.getString(ARG_DESCRIPTION) ?: "No description available"
        // Устанавливаем текст в TextView
        binding.textForAdditionalPage.text = description

        // Обработчики нажатий на кнопки
        binding.arrowBack.setOnClickListener {
            back()
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

    private fun toFavoritesScreen() {
        val favoritesFragment = FavoritesFragment()
        parentFragmentManager.beginTransaction().replace(R.id.fragment_container, favoritesFragment)
            .addToBackStack(null).commit()
    }

    private fun back() {
        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}