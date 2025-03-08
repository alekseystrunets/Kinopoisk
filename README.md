# Kinopoisk App

This project is a mobile application for searching and viewing information about films using the Kinopoisk service API. The application is developed as a pet project to demonstrate skills in working with Android development, Retrofit, Hilt and modern approaches to creating applications.

---

## Application Description

The application allows you to:

- View a list of movies by different categories (e.g. "Best Movies", "New Releases", "Action Movies", etc.).
- Go to the movie page for detailed information.
- Displays movies by different parameters (release year, genre, rating, etc.).
- Save your favorite movies to your favorites (if implemented).

---

## Startup Requirements

To start the project you need:

1. Register on the website [Kinopoisk.dev](https://kinopoisk.dev/) and get an API token.
2. Insert the received token into the `apiKey` field in the `HomeViewModel` class:
   ```kotlin
   private val apiKey = "YOUR_API_KEY"
   
 ## Technologies and tools

- **Programming Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **Network Requests**: Retrofit
- **Dependency Injection**: Hilt
- **UI**: XML
- **Local Database**: Room
- **Async**: Kotlin Coroutines
- **Local Storage**: SharedPreferences
- **Displaying Lists**: RecyclerView


