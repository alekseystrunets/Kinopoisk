data class ApiResponse(
    val docs: List<Film>, // Обратите внимание: в документации поле называется "docs"
    val total: Int,
    val limit: Int,
    val page: Int,
    val pages: Int
)

data class Film(
    val id: Int,
    val name: String,
    val alternativeName: String?,
    val year: Int,
    val description: String?,
    val rating: Rating,
    val poster: Poster,
    val genres: List<Genre>,
    val countries: List<Country>
)

data class Rating(
    val kp: Double,
    val imdb: Double,
    val tmdb: Double
)

data class Poster(
    val url: String,
    val previewUrl: String?
)

data class Genre(
    val name: String
)

data class Country(
    val name: String
)