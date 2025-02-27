package com.example.kinopoisk.presentation.ui.fragment

import UserAccountFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinopoisk.R
import com.example.kinopoisk.databinding.FragmentFavoritesBinding
import com.example.kinopoisk.presentation.Favorites
import com.example.kinopoisk.presentation.adapter.FavoritesFragmentAdapter

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private var recyclerView: RecyclerView? = null
    private var adapter: FavoritesFragmentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the adapter with a sample list of favorites
        val favoritesList = listOf(
            Favorites("Movie 1", "https://example.com/image1.jpg"),
            Favorites("Movie 2", "https://example.com/image2.jpg"),
            Favorites("Movie 3", "https://example.com/image2.jpg"),
            Favorites("Movie 4", "https://example.com/image2.jpg"),
            Favorites("Movie 5", "https://example.com/image2.jpg"),
            Favorites("Movie 6", "https://example.com/image2.jpg"),
            Favorites("Movie 7", "https://example.com/image2.jpg")

        )
        adapter = FavoritesFragmentAdapter(favoritesList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)

        binding.buttonHome.setOnClickListener {
            toHomeScreen()
        }

        binding.buttonAccount.setOnClickListener {
            toAccountScreen()
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
}
