package com.alvd.movies.domain.usecases

import com.example.movies.movies.model.app_state.AppState
import com.alvd.movies.domain.models.Movies
import com.alvd.movies.domain.repository.MovieRepository

class SearchMovies(private val repository: MovieRepository) {
    suspend fun execute(query: String): AppState<Movies> =
        repository.searchMovie(query)
}