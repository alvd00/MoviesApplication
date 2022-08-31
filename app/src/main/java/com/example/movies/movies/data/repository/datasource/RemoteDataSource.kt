package com.alvd.movies.data.repository.datasource

import com.example.movies.movies.model.app_state.AppState
import com.alvd.movies.domain.models.Actors
import com.alvd.movies.domain.models.MovieItem
import com.alvd.movies.domain.models.Movies


interface RemoteDataSource {
    suspend fun getMoviesTop(adult: Boolean, page: Int): AppState<Movies>
    suspend fun getMovieItem(movieId: Int): AppState<MovieItem>
    suspend fun getActorsList(movieId: Int): AppState<Actors>
    suspend fun searchMovie(query: String): AppState<Movies>
}