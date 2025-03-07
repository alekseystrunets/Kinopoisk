package com.example.kinopoisk.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.kinopoisk.data.db.dao.UserDao
import com.example.kinopoisk.data.db.entity.Favorites
import com.example.kinopoisk.data.db.entity.User
import com.example.kinopoisk.data.db.entity.UserFilm

@Database(
    entities = [User::class, Favorites::class, UserFilm::class],
    version = 3, // Увеличьте версию
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kinopoisk_database"
                ).fallbackToDestructiveMigration() // Удаляет старую базу данных при обновлении схемы
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}