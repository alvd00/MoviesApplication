package com.alvd.movies.ui.movies

import androidx.lifecycle.MutableLiveData
import com.example.movies.movies.model.app_state.AppState
import com.alvd.movies.domain.models.Movies
import com.alvd.movies.domain.usecases.GetMoviesTop
import com.alvd.movies.domain.usecases.SearchMovies
import com.alvd.movies.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val moviesLiveData: MutableLiveData<AppState<Movies>> = MutableLiveData<AppState<Movies>>(),
    private val getMoviesTop: GetMoviesTop,
    private val searchMoviesUseCase: SearchMovies
) : BaseViewModel() {

    private val allMovies: ArrayList<Movies.Movie> = arrayListOf()

    private var currentPage: Int = ONE_VALUE

    fun setCurrentPage(value: Int, totalPage: Int) {
        if (value < totalPage) {
            currentPage = value.plus(ONE_VALUE)
        }
    }

    override fun handleError(throwable: Throwable) {}

    fun getMoviesLiveData() = moviesLiveData

    fun getMoviesTop(adult: Boolean = false, page: Int = currentPage): Job =
        viewModelScopeCoroutine.launch {
            getMoviesLiveData().postValue(AppState.Loading)
            val movies = getMoviesTop.execute(adult, page)
            if (movies is AppState.Success) {
                when (val moviesList = movies.data) {
                    is Movies -> {
                        allMovies.addAll(moviesList.movies)
                        getMoviesLiveData().postValue(
                            AppState.Success<Movies>(moviesList.copy(movies = allMovies))
                        )
                    }
                }
            }
        }

    fun searchMovies(query: String): Job =
        viewModelScopeCoroutine.launch {
            coroutineContext.cancelChildren()
            allMovies.clear()
            setCurrentPage(ONE_VALUE, ONE_VALUE)
            getMoviesLiveData().postValue(AppState.Loading)
            val movies = searchMoviesUseCase.execute(query)
            getMoviesLiveData().postValue(movies)
        }

    companion object {
        private const val ONE_VALUE = 1
    }
}