package com.example.kinopoisk.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinopoisk.databinding.RecyclerViewFirstItemBinding
import com.example.kinopoisk.presentation.Film
import com.example.kinopoisk.presentation.interfaices.OnFilmClickListener

class CategoriesAdapter(
    private var categories: Map<String, List<Film>>, // Используем Map
    private val listener: OnFilmClickListener
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(private val binding: RecyclerViewFirstItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(categoryName: String, films: List<Film>) {
            binding.discriptionAboutRecycler.text = categoryName
            binding.thecondRecycler.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.thecondRecycler.adapter = FilmsAdapter(films, listener)
            Log.d("CategoriesAdapter", "Привязана категория: $categoryName")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = RecyclerViewFirstItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryName = categories.keys.elementAt(position)
        val films = categories[categoryName] ?: emptyList()
        holder.bind(categoryName, films)
    }

    override fun getItemCount(): Int = categories.size

    fun updateData(newCategories: Map<String, List<Film>>) {
        this.categories = newCategories
        notifyDataSetChanged() // Уведомляем RecyclerView об изменении данных
        Log.d("CategoriesAdapter", "Данные обновлены, количество категорий: ${newCategories.size}")
    }
}
