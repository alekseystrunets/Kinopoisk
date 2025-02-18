package com.example.kinopoisk.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinopoisk.R
import com.example.kinopoisk.databinding.FragmentAdditionalPageFilmBinding
import com.example.kinopoisk.databinding.FragmentFilmPageBinding
import com.example.kinopoisk.presentation.Actors
import com.example.kinopoisk.presentation.adapter.FilmPageFragmentAdapter

class FilmPageFragment : Fragment() {

    private var _binding: FragmentFilmPageBinding? = null
    private val binding get() = _binding!!

    private  var recyclerView: RecyclerView? = null
    private  var adapter: FilmPageFragmentAdapter? = null

    private val listOfActors = mutableListOf(
        Actors("Actor 1", ""),
        Actors("Actor 2", ""),
        Actors("Actor 3", "")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = FilmPageFragmentAdapter(listOfActors)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFilmPageBinding.inflate(inflater,container,false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       recyclerView =  binding.recVwActors

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

    }
