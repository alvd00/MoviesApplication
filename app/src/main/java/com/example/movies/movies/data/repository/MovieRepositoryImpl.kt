package com.alvd.movies.data.repository

import com.alvd.movies.data.repository.datasource.RemoteDataSource
import com.example.movies.movies.model.app_state.AppState
import com.alvd.movies.domain.models.Actors
import com.alvd.movies.domain.models.MovieItem
import com.alvd.movies.domain.models.Movies
import com.alvd.movies.domain.repository.MovieRepository


class MovieRepositoryImpl(private val dataSource: RemoteDataSource) : MovieRepository {
    override suspend fun getMoviesTop(adult: Boolean, page: Int): AppState<Movies> =
        dataSource.getMoviesTop(adult, page)

    override suspend fun getMovieItem(movieId: Int): AppState<MovieItem> =
        dataSource.getMovieItem(movieId)

    override suspend fun getActors(movieId: Int): AppState<Actors> =
        dataSource.getActorsList(movieId)

    override suspend fun searchMovie(query: String): AppState<Movies> =
        dataSource.searchMovie(query)
}