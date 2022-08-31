package com.alvd.movies.application

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.alvd.movies.di.Di
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(
                listOf(
                    Di.viewModelModule(),
                    Di.apiModule(),
                    Di.repositoryModule(),
                    Di.useCasesModule()
                )
            )
        }
    }
}