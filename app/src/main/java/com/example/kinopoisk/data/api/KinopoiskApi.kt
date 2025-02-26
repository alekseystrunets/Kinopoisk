
import com.example.kinopoisk.presentation.Film
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface KinopoiskApi {

    @GET("v1.4/movie")
    suspend fun getMovies(
        @Header("X-API-KEY") apiKey: String,
        @Query("year") year: String? = null, // Диапазон лет
        @Query("genres.name") genre: String? = null, // Несколько жанров
        @Query("rating.kp") rating: String? = null, // Диапазон рейтинга
        @Query("premiere.russia") premiereDate: String? = null, // Диапазон дат релиза
        @Query("countries.name") countries: String? = null, // Несколько стран
        @Query("limit") limit: Int = 3, // Запрашиваем только 3 фильма
        @Query("page") page: Int = 1

    ): Response<ApiResponse>
}

