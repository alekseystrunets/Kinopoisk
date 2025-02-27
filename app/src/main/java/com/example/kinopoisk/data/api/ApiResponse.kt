import com.example.kinopoisk.presentation.Film

data class ApiResponse(
    val docs: List<Film>,
    val total: Int,
    val limit: Int,
    val page: Int,
    val pages: Int
)
