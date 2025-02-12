package com.example.kinopoisk.presentation.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kinopoisk.R
import com.example.kinopoisk.presentation.ui.fragment.LoadingFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.splash_screen)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            // Загружаем LoadingFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoadingFragment())
                .commit()
        }
    }

}