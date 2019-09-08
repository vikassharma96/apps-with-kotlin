package com.teckudos.devappswithkotlin.apparchitecture.persistence

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.teckudos.devappswithkotlin.R


/**
 * The SleepQualityTracker app is a demo app that helps you collect information about your sleep.
 * - Start time, end time, quality, and time slept
 *
 * This app demonstrates the following views and techniques:
 * - Room database, DAO, and Coroutines
 *
 * It also uses and builds on the following techniques from previous lessons:
 * - Transformation map
 * - Data Binding in XML files
 * - ViewModel Factory
 * - Using Backing Properties to protect MutableLiveData
 * - Observable state LiveData variables to trigger navigation
 */

/**
 * This sleep tracking activity is just a container for our fragments,
 * where the real action is.
 */
class SleepTrackerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sleep_tracker)
    }
}
