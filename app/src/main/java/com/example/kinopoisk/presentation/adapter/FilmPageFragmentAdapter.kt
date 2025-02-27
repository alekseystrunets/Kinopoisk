package com.example.kinopoisk.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinopoisk.R
import com.example.kinopoisk.presentation.Actors

class FilmPageFragmentAdapter(
    private var actors: MutableList<Actors>
) : RecyclerView.Adapter<FilmPageFragmentAdapter.ActorViewHolder>() {

    // ViewHolder для актеров
    inner class ActorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val actorName: TextView = itemView.findViewById(R.id.name_of_actor)
        private val actorPhoto: ImageView = itemView.findViewById(R.id.actors_img)

        fun bind(actor: Actors) {
            actorName.text = actor.name
            Glide.with(itemView.context)
                .load(actor.photo)
                .into(actorPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_actors_item, parent, false)
        return ActorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.bind(actors[position])
    }

    override fun getItemCount(): Int {
        return actors.size
    }

    // Метод для обновления списка актеров
    fun updateActors(newActors: List<Actors>) {
        Log.d("FilmPageFragmentAdapter", "Updating actors: $newActors") // Логируем новые данные
        actors.clear()
        actors.addAll(newActors)
        notifyDataSetChanged()
    }
}