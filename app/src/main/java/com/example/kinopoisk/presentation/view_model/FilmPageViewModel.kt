package com.example.kinopoisk.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.data.db.AppDatabase
import com.example.kinopoisk.data.db.entity.Favorites
import com.example.kinopoisk.data.db.entity.UserFilm
import com.example.kinopoisk.data.db.repository.UserRepository
import com.example.kinopoisk.presentation.Film
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FilmPageViewModel @Inject constructor(
    application: Application,
    private val userRepository: UserRepository // Внедряем UserRepository через конструктор
) : AndroidViewModel(application) {

    // LiveData для данных о фильме
    private val _film = MutableLiveData<Film?>()
    val film: LiveData<Film?> get() = _film

    // LiveData для сообщений Toast
    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    // Установить данные о фильме
    fun setFilm(film: Film) {
        _film.value = film
    }

    // Добавить фильм в избранное
    fun addToFavorites(context: Context, film: Film) {
        viewModelScope.launch {
            // Получаем email текущего пользователя из SharedPreferences
            val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val userEmail = sharedPreferences.getString("user_email", null)

            if (userEmail != null) {
                // Проверяем, есть ли фильм уже в избранном
                val filmId = film.id ?: run {
                    _toastMessage.postValue("Error: Movie ID is missing.")
                    return@launch
                }

                val isExists = isFavoriteExists(context, filmId, userEmail)
                if (isExists) {
                    _toastMessage.postValue("The film is already in favorites")
                    return@launch
                }

                // Создаем объект Favorites
                val favorite = Favorites(
                    id = filmId,
                    name = film.name ?: "Unknown",
                    year = film.year ?: 0,
                    description = film.description ?: "No description",
                    posterUrl = film.poster?.url ?: "",
                    userEmail = userEmail,
                    rating = film.rating?.kp ?: 0.0,
                    votes = film.votes?.kp ?: 0
                )

                // Сохраняем фильм в базу данных
                try {
                    withContext(Dispatchers.IO) {
                        userRepository.addFavoriteToUser(userEmail, favorite)
                    }

                    _toastMessage.postValue("Movie added to favorites")
                } catch (e: Exception) {
                    _toastMessage.postValue("Error adding movie to favorites")
                    Log.e("FilmPageViewModel", "Ошибка при добавлении фильма в избранное", e)
                }
            } else {
                _toastMessage.postValue("Save 'User' to dictionary")
            }
        }
    }

    // Проверка, есть ли фильм в избранном
    private suspend fun isFavoriteExists(context: Context, filmId: Int, userEmail: String): Boolean {
        return withContext(Dispatchers.IO) {
            val database = AppDatabase.getDatabase(context)
            val favorite = database.userDao().getFavoriteByIdAndUser(filmId, userEmail)
            favorite != null
        }
    }
}