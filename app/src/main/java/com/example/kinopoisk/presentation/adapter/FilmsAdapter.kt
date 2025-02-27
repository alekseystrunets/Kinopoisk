package com.example.kinopoisk.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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
            // Если title равен null, используем заглушку
            val title = film.name ?: "Название отсутствует"
            binding.titleOfTheFilm.text = title

            // Если imageUrl равен null, используем заглушку
            val imageUrl = film.imageUrl ?: R.drawable.baseline_do_disturb_alt_24
            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.drawable.baseline_downloading_24)
                .error(R.drawable.baseline_do_disturb_alt_24)
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Кэшируем изображения
                .into(binding.filmImage)

            binding.root.setOnClickListener {
                listener.onFilmClick(film)
            }
            Log.d("FilmsAdapter", "Привязан фильм: $title")
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
