package com.example.myrunique

import android.app.Application
import com.example.auth.data.di.authDataModule
import com.example.auth.presentation.di.authViewModelModule
import com.example.myrunique.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class MyRuniqueApp: Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@MyRuniqueApp)
            modules(
                authDataModule,
                authViewModelModule,
                appModule
            )
        }
    }
}