package com.alvd.movies.domain.usecases

import com.example.movies.movies.model.app_state.AppState
import com.alvd.movies.domain.models.MovieItem
import com.alvd.movies.domain.repository.MovieRepository


class GetMovieItem(private val repository: MovieRepository) {
    suspend fun execute(movieId: Int): AppState<MovieItem> =
        repository.getMovieItem(movieId)
}