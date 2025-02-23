package com.example.kinopoisk.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinopoisk.databinding.ThecondRecyclerViewItemBinding
import com.example.kinopoisk.presentation.Film

class FilmsAdapter(private val films: List<Film>) : RecyclerView.Adapter<FilmsAdapter.FilmViewHolder>() {

    inner class FilmViewHolder(private val binding: ThecondRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(film: Film) {
            Glide.with(binding.root.context)
                .load(film.imageUrl)
                .into(binding.filmImage)
            binding.titleOfTheFilm.text = film.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val binding = ThecondRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.bind(films[position])
    }

    override fun getItemCount(): Int = films.size
}
