package com.alvd.movies.di

import android.os.Build
import androidx.annotation.RequiresApi
import com.alvd.movies.data.api.ApiInterceptor
import com.alvd.movies.data.api.ApiService
import com.alvd.movies.data.repository.MovieRepositoryImpl
import com.alvd.movies.data.repository.datasource.RemoteDataSource
import com.alvd.movies.data.repository.datasource.RemoteDataSourceImpl
import com.alvd.movies.domain.repository.MovieRepository
import com.alvd.movies.domain.usecases.GetActors
import com.alvd.movies.domain.usecases.GetMovieItem
import com.alvd.movies.domain.usecases.GetMoviesTop
import com.alvd.movies.domain.usecases.SearchMovies
import com.alvd.movies.ui.detail.MovieItemViewModel
import com.alvd.movies.ui.movies.MoviesViewModel
import com.alvd.movies.utils.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Di {
    fun viewModelModule() = module {

        viewModel() {
            MoviesViewModel(
                getMoviesTop = get(),
                searchMoviesUseCase = get()
            )
        }

        viewModel() {
            MovieItemViewModel(
                getMovieDetailByIdUseCase = get(),
                getActors = get()
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun apiModule() = module {
        single<Interceptor> {
            ApiInterceptor()
        }

        single<Gson> {
            GsonBuilder()
                .create()
        }

        single<ApiService> {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                            HttpLoggingInterceptor.Level.BODY
                        })
                        .build()
                )
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(get()))
                .build()
                .create(ApiService::class.java)
        }
    }

    fun repositoryModule() = module {
        single<MovieRepository>() {
            MovieRepositoryImpl(
                dataSource = get()
            )
        }

        single<RemoteDataSource> {
            RemoteDataSourceImpl(movieApi = get())
        }
    }

    fun useCasesModule() = module {
        factory {
            GetMoviesTop(repository = get())
        }

        factory {
            GetMovieItem(repository = get())
        }

        factory {
            GetActors(repository = get())
        }

        factory {
            SearchMovies(repository = get())
        }
    }
}