package com.example.myrunique

import android.app.Application
import com.example.auth.data.di.authDataModule
import com.example.auth.presentation.di.authViewModelModule
import com.example.core.data.di.coreDataModule
import com.example.core.database.di.databaseModule
import com.example.myrunique.di.appModule
import com.example.run.location.di.locationModule
import com.example.run.network.di.networkModule
import com.example.run.presentation.di.runPresentationModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class MyRuniqueApp: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

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
                runPresentationModule,
                appModule,
                coreDataModule,
                locationModule,
                databaseModule,
                networkModule
            )
        }
    }
}
