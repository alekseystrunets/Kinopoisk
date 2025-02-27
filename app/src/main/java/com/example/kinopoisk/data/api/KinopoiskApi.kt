
import com.example.kinopoisk.presentation.ActorsResponse
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
        @Query("year") year: String? = null,
        @Query("genres.name") genre: String? = null,
        @Query("rating.kp") rating: String? = null,
        @Query("premiere.russia") premiereDate: String? = null,
        @Query("countries.name") countries: String? = null,
        @Query("limit") limit: Int = 3,
        @Query("page") page: Int = 1
    ): Response<ApiResponse>

    @GET("v1.4/movie/{id}/persons")
    suspend fun getActors(
        @Path("id") filmId: Int,
        @Header("X-API-KEY") apiKey: String
    ): Response<ActorsResponse>
}
