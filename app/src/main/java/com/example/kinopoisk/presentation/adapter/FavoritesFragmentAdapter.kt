package com.example.kinopoisk.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinopoisk.R
import com.example.kinopoisk.data.db.entity.Favorites

class FavoritesFragmentAdapter(
    private val favorites: List<Favorites>,
    private val onItemClick: (Favorites) -> Unit // Обработчик клика
) : RecyclerView.Adapter<FavoritesFragmentAdapter.FavoritesViewHolder>() {

    inner class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filmImage: ImageView = itemView.findViewById(R.id.recycler_item_img)
        val filmName: TextView = itemView.findViewById(R.id.name_of_the_film)

        init {
            // Обработчик клика на элемент
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(favorites[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_favorites_films_item, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val favorite = favorites[position]

        // Устанавливаем название фильма
        holder.filmName.text = favorite.name

        // Загружаем изображение с помощью Glide
        Glide.with(holder.itemView.context)
            .load(favorite.posterUrl)
            .placeholder(R.drawable.baseline_do_disturb_alt_24)
            .into(holder.filmImage)
    }

    override fun getItemCount(): Int {
        return favorites.size
    }
}