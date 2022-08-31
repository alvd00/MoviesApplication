package com.alvd.movies.ui.detail

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.example.movies.movies.model.app_state.AppState
import com.alvd.movies.domain.models.Actors
import com.alvd.movies.domain.models.MovieItem
import com.alvd.movies.domain.usecases.GetActors
import com.alvd.movies.domain.usecases.GetMovieItem
import com.alvd.movies.ui.base.BaseViewModel

/**
 * @author Borisov Andrey on 27.06.2022
 **/

class MovieItemViewModel(
    private val movieLiveData: MutableLiveData<AppState<MovieItem>> = MutableLiveData<AppState<MovieItem>>(),
    private val actorLiveData: MutableLiveData<AppState<Actors>> = MutableLiveData<AppState<Actors>>(),
    private val getMovieDetailByIdUseCase: GetMovieItem,
    private val getActors: GetActors,
) : BaseViewModel() {

    override fun handleError(throwable: Throwable) {}

    fun getMovieLiveData() = movieLiveData
    fun getActorLiveData() = actorLiveData

    fun getMovieDetail(movieId: Int): Job =
        viewModelScopeCoroutine.launch {
            getMovieLiveData().postValue(AppState.Loading)
            getMovieLiveData().postValue(getMovieDetailByIdUseCase.execute(movieId))
        }

    fun getActors(movieId: Int): Job =
        viewModelScopeCoroutine.launch {
            getActorLiveData().postValue(AppState.Loading)
            getActorLiveData().postValue(getActors.execute(movieId))
        }
}