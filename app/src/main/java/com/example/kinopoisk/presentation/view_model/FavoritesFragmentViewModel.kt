package com.example.kinopoisk.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.data.db.AppDatabase
import com.example.kinopoisk.data.db.entity.Favorites
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    // LiveData для списка избранных фильмов
    private val _favorites = MutableLiveData<List<Favorites>>()
    val favorites: LiveData<List<Favorites>> get() = _favorites

    // LiveData для сообщений Toast
    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    private val database = AppDatabase.getDatabase(application)

    // Загрузка избранных фильмов
    fun loadFavorites(userEmail: String) {
        viewModelScope.launch {
            val favoritesFromDb = withContext(Dispatchers.IO) {
                database.userDao().getFavoritesForUser(userEmail)
            }
            _favorites.postValue(favoritesFromDb)
        }
    }

    // Удаление фильма из избранного
    fun deleteFavorite(userEmail: String, favorite: Favorites) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Удаляем связь пользователя с фильмом
                database.userDao().deleteUserFilm(userEmail, favorite.id)
                // Удаляем фильм из таблицы избранных
                database.userDao().deleteFavorite(favorite)
            }
            // После удаления фильма загружаем обновленный список избранных
            loadFavorites(userEmail)
            _toastMessage.postValue("Фильм удален из избранного")
        }
    }
}