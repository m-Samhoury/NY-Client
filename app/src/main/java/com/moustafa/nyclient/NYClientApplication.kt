package com.moustafa.nyclient

import android.app.Application
import com.moustafa.nyclient.di.repositoryModule
import com.moustafa.nyclient.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * @author moustafasamhoury
 * created on Sunday, 20 Oct, 2019
 */

class NYClientApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NYClientApplication)

            if (BuildConfig.DEBUG) {
                androidLogger()
            }

            modules(listOf(repositoryModule, viewModelsModule))
        }
    }
}
