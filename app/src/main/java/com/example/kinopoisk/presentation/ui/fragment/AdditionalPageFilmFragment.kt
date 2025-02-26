package com.example.kinopoisk.presentation.ui.fragment

import HomeFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kinopoisk.R
import com.example.kinopoisk.databinding.FragmentAdditionalPageFilmBinding
import com.example.kinopoisk.databinding.FragmentFavoritesBinding

class AdditionalPageFilmFragment : Fragment() {

    private var _binding: FragmentAdditionalPageFilmBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAdditionalPageFilmBinding.inflate(inflater,container,false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arrowBack.setOnClickListener{
            back()
        }

        binding.buttonHome.setOnClickListener{
            toHomeScreen()
        }

        binding.buttonAccount.setOnClickListener{
            toAccountScreen()
        }

        binding.buttonFavorites.setOnClickListener{
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

    private fun back(){
        parentFragmentManager.popBackStack()
    }

}