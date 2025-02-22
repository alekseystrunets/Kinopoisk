package com.example.kinopoisk.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinopoisk.R
import com.example.kinopoisk.presentation.Favorites

class FavoritesFragmentAdapter(
    private val list: List<Favorites>
) : RecyclerView.Adapter<FavoritesFragmentAdapter.NewViewHolder>() {

    class NewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameOfTheFilm: AppCompatTextView = itemView.findViewById(R.id.name_of_the_film)
        val image: AppCompatImageView = itemView.findViewById(R.id.recycler_item_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewViewHolder {
        return NewViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_favorites_films_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
        val favoriteFilm = list[position]
        holder.nameOfTheFilm.text = favoriteFilm.nameOfTheFilm
        Glide.with(holder.itemView.context)
            .load(favoriteFilm.imageUrl)
            .placeholder(R.drawable.baseline_downloading_24)
            .error(R.drawable.baseline_do_disturb_alt_24)
            .into(holder.image)
    }

    override fun getItemCount(): Int = list.size
}
