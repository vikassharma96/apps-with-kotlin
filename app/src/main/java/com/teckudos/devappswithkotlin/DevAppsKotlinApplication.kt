package com.teckudos.devappswithkotlin

import android.app.Application
import timber.log.Timber

class DevAppsKotlinApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}