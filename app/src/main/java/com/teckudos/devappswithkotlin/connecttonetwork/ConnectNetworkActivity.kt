package com.teckudos.devappswithkotlin.connecttonetwork

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.teckudos.devappswithkotlin.R

class ConnectNetworkActivity : AppCompatActivity() {

    /**
     * Our ConnectNetworkActivity is only responsible for setting the content view that contains the
     * Navigation Host.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_network)
    }
}
