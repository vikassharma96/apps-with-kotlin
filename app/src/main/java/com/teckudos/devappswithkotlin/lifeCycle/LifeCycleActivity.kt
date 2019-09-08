package com.teckudos.devappswithkotlin.lifeCycle

import android.content.ActivityNotFoundException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import com.teckudos.devappswithkotlin.R
import com.teckudos.devappswithkotlin.databinding.ActivityLifeCycleBinding
import timber.log.Timber

class LifeCycleActivity : AppCompatActivity() {

    private var revenue = 0
    private var dessertsSold = 0
    private lateinit var timer: Timer

    // Contains all the views
    private lateinit var binding: ActivityLifeCycleBinding

    /** Dessert Data **/

    /**
     * Simple data class that represents a dessert. Includes the resource id integer associated with
     * the image, the price it's sold for, and the startProductionAmount, which determines when
     * the dessert starts to be produced.
     */
    data class Dessert(val imageId: Int, val price: Int, val startProductionAmount: Int)

    // Create a list of all desserts, in order of when they start being produced
    private val allDesserts = listOf(
        Dessert(R.drawable.cupcake, 5, 0),
        Dessert(R.drawable.donut, 10, 1),
        Dessert(R.drawable.eclair, 15, 2),
        Dessert(R.drawable.froyo, 30, 3),
        Dessert(R.drawable.gingerbread, 50, 4),
        Dessert(R.drawable.honeycomb, 100, 4),
        Dessert(R.drawable.icecreamsandwich, 500, 5),
        Dessert(R.drawable.jellybean, 1000, 5),
        Dessert(R.drawable.kitkat, 2000, 5),
        Dessert(R.drawable.lollipop, 3000, 6),
        Dessert(R.drawable.marshmallow, 4000, 6),
        Dessert(R.drawable.nougat, 5000, 6),
        Dessert(R.drawable.oreo, 6000, 7)
    )
    private var currentDessert = allDesserts[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate Called")

        // Use Data Binding to get reference to the views
        binding = DataBindingUtil.setContentView(this, R.layout.activity_life_cycle)

        binding.dessertButton.setOnClickListener {
            onDessertClicked()
        }

        // Setup dessertTimer, passing in the lifecycle
        timer = Timer(this.lifecycle)

        // If there is a savedInstanceState bundle, then you're "restarting" the activity
        // If there isn't a bundle, then it's a "fresh" start
        if (savedInstanceState != null) {
            // Get all the game state information from the bundle, set it
            revenue = savedInstanceState.getInt(KEY_REVENUE, 0)
            dessertsSold = savedInstanceState.getInt(KEY_DESSERT_SOLD, 0)
            timer.secondsCount = savedInstanceState.getInt(KEY_TIMER_SECONDS, 0)
            showCurrentDessert()

        }

        // Set the TextViews to the right values
        binding.revenue = revenue
        binding.amountSold = dessertsSold

        // Make sure the correct dessert is showing
        binding.dessertButton.setImageResource(currentDessert.imageId)
    }

    /**
     * Updates the score when the dessert is clicked. Possibly shows a new dessert.
     */
    private fun onDessertClicked() {

        // Update the score
        revenue += currentDessert.price
        dessertsSold++

        binding.revenue = revenue
        binding.amountSold = dessertsSold

        // Show the next dessert
        showCurrentDessert()
    }

    /**
     * Determine which dessert to show.
     */
    private fun showCurrentDessert() {
        var newDessert = allDesserts[0]
        for (dessert in allDesserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                newDessert = dessert
            }
            // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
            // you'll start producing more expensive desserts as determined by startProductionAmount
            // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
            // than the amount sold.
            else break
        }

        // If the new dessert is actually different than the current dessert, update the image
        if (newDessert != currentDessert) {
            currentDessert = newDessert
            binding.dessertButton.setImageResource(newDessert.imageId)
        }
    }

    /**
     * Menu methods
     */
    private fun onShare() {
        val shareIntent = ShareCompat.IntentBuilder.from(this)
            .setText(getString(R.string.share_text, dessertsSold, revenue))
            .setType("text/plain")
            .intent
        try {
            startActivity(shareIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.sharing_not_available),
                Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shareMenuButton -> onShare()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Called when the user navigates away from the app but might come back
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_REVENUE, revenue)
        outState.putInt(KEY_DESSERT_SOLD, dessertsSold)
        outState.putInt(KEY_TIMER_SECONDS, timer.secondsCount)
        Timber.i("onSaveInstanceState Called")
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Timber.i("onRestoreInstanceState Called")
    }

    /** Lifecycle Methods **/
    override fun onStart() {
        super.onStart()
        Timber.i("onStart Called")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume Called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy Called")
    }

    override fun onRestart() {
        super.onRestart()
        Timber.i("onRestart Called")
    }
}

/** onSaveInstanceState Bundle Keys **/
const val KEY_REVENUE = "revenue_key"
const val KEY_DESSERT_SOLD = "dessert_sold_key"
const val KEY_TIMER_SECONDS = "timer_seconds_key"
