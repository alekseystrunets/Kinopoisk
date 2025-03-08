package com.example.kinopoisk.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.data.db.entity.Favorites
import com.example.kinopoisk.data.db.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    application: Application,
    private val userRepository: UserRepository // Внедряем UserRepository через конструктор
) : AndroidViewModel(application) {

    // LiveData для списка избранных фильмов
    private val _favorites = MutableLiveData<List<Favorites>>()
    val favorites: LiveData<List<Favorites>> get() = _favorites

    // LiveData для сообщений Toast
    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    // Загрузка избранных фильмов
    fun loadFavorites(userEmail: String) {
        viewModelScope.launch {
            val favoritesFromDb = withContext(Dispatchers.IO) {
                userRepository.getFavoritesForUser(userEmail)
            }
            _favorites.postValue(favoritesFromDb)
        }
    }

    // Удаление фильма из избранного
    fun deleteFavorite(userEmail: String, favorite: Favorites) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.deleteFavorite(userEmail, favorite)
            }
            // После удаления фильма загружаем обновленный список избранных
            loadFavorites(userEmail)
            _toastMessage.postValue("The film has been removed from favorites")
        }
    }
}