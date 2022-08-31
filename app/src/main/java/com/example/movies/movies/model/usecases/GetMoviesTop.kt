package com.alvd.movies.domain.usecases

import com.example.movies.movies.model.app_state.AppState
import com.alvd.movies.domain.models.Movies
import com.alvd.movies.domain.repository.MovieRepository

class GetMoviesTop(private val repository: MovieRepository) {
    suspend fun execute(adult: Boolean = false, page: Int): AppState<Movies> =
        repository.getMoviesTop(adult, page)
}