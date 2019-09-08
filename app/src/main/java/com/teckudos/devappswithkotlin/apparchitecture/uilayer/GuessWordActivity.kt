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
