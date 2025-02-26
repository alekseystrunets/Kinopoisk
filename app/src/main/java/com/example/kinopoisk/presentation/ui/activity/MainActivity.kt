package com.example.kinopoisk.presentation.ui.activity

import LoginFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kinopoisk.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            // Загружаем LoadingFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .commit()
        }
    }

}