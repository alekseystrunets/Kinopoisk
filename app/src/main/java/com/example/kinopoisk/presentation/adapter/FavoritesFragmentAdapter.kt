package com.example.kinopoisk.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinopoisk.R
import com.example.kinopoisk.data.db.entity.Favorites

class FavoritesFragmentAdapter(
    private var favorites: List<Favorites>,
    private val onItemClick: (Favorites) -> Unit,
    private val onMenuItemClick: (Favorites, Int) -> Unit
) : RecyclerView.Adapter<FavoritesFragmentAdapter.FavoritesViewHolder>() {

    inner class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filmImage: ImageView = itemView.findViewById(R.id.recycler_item_img)
        val filmName: TextView = itemView.findViewById(R.id.name_of_the_film)
        val menuButton: AppCompatImageButton = itemView.findViewById(R.id.menu_for_favorites)

        init {
            // Обработчик клика на элемент
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(favorites[position])
                }
            }

            // Обработчик клика на кнопку меню
            menuButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    showPopupMenu(menuButton, favorites[position])
                }
            }
        }

        // Показ PopupMenu
        private fun showPopupMenu(view: View, favorite: Favorites) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.menuInflater.inflate(R.menu.favorites_menu, popupMenu.menu)

            // Устанавливаем кастомный макет для каждого пункта меню
            val menu = popupMenu.menu
            for (i in 0 until menu.size()) {
                val menuItem = menu.getItem(i)
                val customMenuItemView = LayoutInflater.from(view.context)
                    .inflate(R.layout.custom_menu_item, null) as LinearLayout
                val textView = customMenuItemView.findViewById<TextView>(R.id.menu_item_text)
                textView.text = menuItem.title
                menuItem.actionView = customMenuItemView
            }

            // Обработчик клика на пункт меню
            popupMenu.setOnMenuItemClickListener { menuItem ->
                onMenuItemClick(favorite, menuItem.itemId)
                true
            }

            popupMenu.show()
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

    // Метод для удаления элемента
    fun removeItem(position: Int) {
        if (position in favorites.indices) {
            val updatedList = favorites.toMutableList()
            updatedList.removeAt(position)
            favorites = updatedList
            notifyItemRemoved(position)
        }
    }

    // Метод для получения позиции фильма
    fun getPosition(favorite: Favorites): Int {
        return favorites.indexOf(favorite)
    }
}