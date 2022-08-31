package com.alvd.movies.domain.usecases

import com.example.movies.movies.model.app_state.AppState
import com.alvd.movies.domain.models.Actors
import com.alvd.movies.domain.repository.MovieRepository


class GetActors(private val repository: MovieRepository) {
    suspend fun execute(movieId: Int): AppState<Actors> =
        repository.getActors(movieId)
}