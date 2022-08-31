package com.example.movies.movies.model.app_state

sealed class AppState<out T> : IAppState {
    data class Success<out T>(val data: T) : AppState<T>()
    data class Error(val error: Throwable) : AppState<Nothing>()
    object Loading : AppState<Nothing>()
}

