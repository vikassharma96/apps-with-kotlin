package com.teckudos.devappswithkotlin.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.teckudos.devappswithkotlin.R
import com.teckudos.devappswithkotlin.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
        val binding =
            DataBindingUtil.setContentView<ActivityGameBinding>(
                this,
                R.layout.activity_game
            )
        drawerLayout = binding.drawerLayout
        navController = this.findNavController(R.id.myNavHostFragment)
        //NavigationUI.setupActionBarWithNavController(this, navController)

        //to setup action bar with nav controller now we need to include drawer layout
        //and we also need to setup navigation ui to know about navigation view
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)

        //onDestinationChangedListener called whenever destination changes
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == controller.graph.startDestination) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        //return navController.navigateUp()
        //here we need to use navigate ui navigate up with drawer layout as a parameter instead of
        //navController.navigateUp() because navigation ui replace up button with drawer button
        return NavigationUI.navigateUp(navController, drawerLayout)
    }
}
