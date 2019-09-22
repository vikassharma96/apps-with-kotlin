package com.teckudos.devappswithkotlin.apparchitecture.uilayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.teckudos.devappswithkotlin.R

/**
 * Creates an Activity that hosts all of the fragments in the app
 */
class GuessWordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess_word)
    }

}

// Lifecycle library includes the lifecycle, lifecycleobserver and lifecycleowner classes it also
// includes viewmodel and livedata classes.
// we can resolve configuration changes issue by using onSaveInstanceState bundle and retreive it in
// onRestoreInstanceState but it but they required to write extra code to store the state in the
// bundle and the logic to retreive the state and it can only store 100kilobytes of data if we risk
// to store to much data it through an exception.

// Application architecture
// Design of an application's classes and relationship between them such that code base is more
// organized, performative in particular scenerio and easier to work with.
// UIController -> ViewModel - do actual decision making, hold data needed for UI and prepare to display
//     =========== LiveData
// Viewmodel - Abstract class that holds app's UI data. Survive configurations changes it has no
// size restrictions. viemodel contain no references to activities, fragments or views.

// LiveData - An observable data holder class that is lifecycle aware