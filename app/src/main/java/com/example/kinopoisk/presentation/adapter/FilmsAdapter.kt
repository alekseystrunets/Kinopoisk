package com.example.kinopoisk.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinopoisk.R
import com.example.kinopoisk.databinding.ThecondRecyclerViewItemBinding
import com.example.kinopoisk.presentation.Film
import com.example.kinopoisk.presentation.interfaices.OnFilmClickListener

class FilmsAdapter(
    private val films: List<Film>,
    private val listener: OnFilmClickListener
) : RecyclerView.Adapter<FilmsAdapter.FilmViewHolder>() {

    inner class FilmViewHolder(private val binding: ThecondRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(film: Film) {
            Glide.with(binding.root.context)
                .load(film.imageUrl)
                .placeholder(R.drawable.baseline_downloading_24)
                .error(R.drawable.baseline_do_disturb_alt_24)
                .into(binding.filmImage)
            binding.titleOfTheFilm.text = film.title
            binding.root.setOnClickListener {
                listener.onFilmClick(film)
            }
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
