package com.example.kinopoisk.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.kinopoisk.R
import com.example.kinopoisk.presentation.Actors

class FilmPageFragmentAdapter(
    val list: List<Actors>
) : RecyclerView.Adapter<FilmPageFragmentAdapter.NewViewHolder>() {

    class NewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<AppCompatTextView>(R.id.name_of_actor)
        val image = itemView.findViewById<AppCompatImageView>(R.id.actors_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewViewHolder {
        return NewViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_actors_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
        val actor = list[position]
        holder.name.text = list[position].name
        Glide.with(holder.itemView.context)
            .load(actor.image) // actor.image — это URL изображения
            .placeholder(R.drawable.baseline_downloading_24) // Плейсхолдер, если изображение загружается
            .error(R.drawable.baseline_do_disturb_alt_24) // Изображение при ошибке загрузки
            .into(holder.image)
    }

    override fun getItemCount(): Int = list.size
}
