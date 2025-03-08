package com.example.kinopoisk.presentation.ui.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kinopoisk.R
import com.example.kinopoisk.presentation.ui.fragment.LoginFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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