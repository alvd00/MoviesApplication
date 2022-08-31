package com.alvd.movies.data.api

import com.alvd.movies.domain.models.Actors
import com.alvd.movies.domain.models.MovieItem
import com.alvd.movies.domain.models.Movies
import com.alvd.movies.utils.API_KEY
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    @GET("movie/top_rated?api_key=${API_KEY}&language=ru-RU")
    fun getMoviesTopAsync(
        @Query("include_adult") adult: Boolean,
        @Query("page") page: Int
    ): Deferred<Movies>

    @GET("movie/{movie_id}?api_key=${API_KEY}&language=ru-RU")
    fun getMovieItemAsync(@Path("movie_id") movieId: Int): Deferred<MovieItem>

    @GET("movie/{movie_id}/casts?api_key=${API_KEY}&language=ru-RU")
    fun getActorsAsync(@Path("movie_id") movieId: Int): Deferred<Actors>

    @GET("search/movie?api_key=${API_KEY}&language=ru-RU")
    fun searchMovieAsync(@Query("query") query: String): Deferred<Movies>
}