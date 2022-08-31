package com.alvd.movies.domain.repository

import com.example.movies.movies.model.app_state.AppState
import com.alvd.movies.domain.models.Actors
import com.alvd.movies.domain.models.MovieItem
import com.alvd.movies.domain.models.Movies


interface MovieRepository {
    suspend fun getMoviesTop(adult: Boolean, page: Int): AppState<Movies>
    suspend fun getMovieItem(movieId: Int): AppState<MovieItem>
    suspend fun getActors(movieId: Int): AppState<Actors>
    suspend fun searchMovie(query: String): AppState<Movies>
}