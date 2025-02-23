package com.example.kinopoisk.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kinopoisk.databinding.RecyclerViewFirstItemBinding
import com.example.kinopoisk.presentation.Film

class CategoriesAdapter(private val categories: List<Pair<String, List<Film>>>) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(private val binding: RecyclerViewFirstItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Pair<String, List<Film>>) {
            binding.discriptionAboutRecycler.text = category.first
            binding.thecondRecycler.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.thecondRecycler.adapter = FilmsAdapter(category.second)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = RecyclerViewFirstItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size
}
