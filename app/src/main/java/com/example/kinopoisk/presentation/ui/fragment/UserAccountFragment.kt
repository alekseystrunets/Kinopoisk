package com.example.kinopoisk.presentation.ui.fragment

import LoginFragmentViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.kinopoisk.R
import com.example.kinopoisk.databinding.FragmentUserAccountBinding
import com.example.kinopoisk.presentation.view_model.UserAccountViewModel

class UserAccountFragment : Fragment() {

    private var _binding: FragmentUserAccountBinding? = null
    private val binding get() = _binding!!
    private var viewModel: UserAccountViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserAccountViewModel::class.java)
        // Получаем email из аргументов
        val email = arguments?.getString("email") ?: return

        // Загружаем данные пользователя
        viewModel?.loadUserData(email)

        // Наблюдаем за данными пользователя
        viewModel?.userData?.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.userAccountLogin.text = user.login
                binding.emailAccount.text = user.email
            }
        }

        // Обработка нажатий на кнопки
        binding.buttonHome.setOnClickListener {
            toHomeScreen()
        }

        binding.buttonFavorites.setOnClickListener {
            toFavoritesScreen()
        }
    }

    private fun toHomeScreen() {
        val homeScreen = HomeFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, homeScreen)
            .addToBackStack(null)
            .commit()
    }

    private fun toFavoritesScreen() {
        val favoritesFragment = FavoritesFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, favoritesFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}