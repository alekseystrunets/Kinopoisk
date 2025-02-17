package com.example.kinopoisk.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinopoisk.R
import com.example.kinopoisk.presentation.Actors
import com.example.kinopoisk.presentation.adapter.FilmPageFragmentAdapter

class FilmPageFragment : Fragment() {

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
    ): View? {

        recyclerView = view?.findViewById(R.id.rec_vw_actors)

        return inflater.inflate(R.layout.fragment_film_page, container, false)
    }

    }
