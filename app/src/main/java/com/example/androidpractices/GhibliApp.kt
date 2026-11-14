package com.example.androidpractices

import android.app.Application
import com.example.androidpractices.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GhibliApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GhibliApp)
            modules(appModule)
        }
    }
}
