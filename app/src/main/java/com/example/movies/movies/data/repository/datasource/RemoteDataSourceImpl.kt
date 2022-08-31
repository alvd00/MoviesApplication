package com.alvd.movies.data.repository.datasource

import com.alvd.movies.data.api.ApiService
import com.example.movies.movies.model.app_state.AppState
import com.alvd.movies.domain.models.Actors
import com.alvd.movies.domain.models.MovieItem
import com.alvd.movies.domain.models.Movies
import com.example.movies.movies.utils.EMPTY_DATA


class RemoteDataSourceImpl(private val movieApi: ApiService) : RemoteDataSource {
    override suspend fun getMoviesTop(adult: Boolean, page: Int): AppState<Movies> =
        try {
            val result = movieApi.getMoviesTopAsync(adult, page).await()

            if (result.movies.isNotEmpty()) {
                AppState.Success(result)
            } else {
                AppState.Error(Exception(EMPTY_DATA))
            }
        } catch (err: Exception) {
            AppState.Error(err)
        }

    override suspend fun getMovieItem(movieId: Int): AppState<MovieItem> =
        try {
            val result = movieApi.getMovieItemAsync(movieId).await()
            AppState.Success(result)
        } catch (err: Exception) {
            AppState.Error(err)
        }

    override suspend fun getActorsList(movieId: Int): AppState<Actors> =
        try {
            val result = movieApi.getActorsAsync(movieId).await()

            if (result.cast.isNotEmpty()) {
                AppState.Success(result)
            } else {
                AppState.Error(Exception(EMPTY_DATA))
            }
        } catch (err: Exception) {
            AppState.Error(err)
        }

    override suspend fun searchMovie(query: String): AppState<Movies> =
        try {
            val result = movieApi.searchMovieAsync(query).await()

            if (result.movies.isNotEmpty()) {
                AppState.Success(result)
            } else {
                AppState.Error(Exception(EMPTY_DATA))
            }
        } catch (error: Exception) {
            AppState.Error(error)
        }
}