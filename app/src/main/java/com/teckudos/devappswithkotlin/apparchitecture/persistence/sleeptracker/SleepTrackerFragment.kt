package com.teckudos.devappswithkotlin.apparchitecture.persistence.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.teckudos.devappswithkotlin.R
import com.teckudos.devappswithkotlin.apparchitecture.persistence.database.SleepDatabase
import com.teckudos.devappswithkotlin.databinding.FragmentSleepTrackerBinding

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class SleepTrackerFragment : Fragment() {

    private lateinit var binding: FragmentSleepTrackerBinding
    private lateinit var sleepTrackerViewModel: SleepTrackerViewModel

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sleep_tracker, container, false
        )

        val application = requireNotNull(this.activity).application

        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao

        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)

        sleepTrackerViewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(SleepTrackerViewModel::class.java)

        binding.sleepTrackerViewModel = sleepTrackerViewModel

        binding.lifecycleOwner = this

        setAdapter()

        setListeners()

        return binding.root
    }

    private fun setAdapter() {
        val manager = GridLayoutManager(activity, 3)
        val adapter = SleepNightAdapter(SleepNightListener { nightId ->
            sleepTrackerViewModel.onSleepNightClicked(nightId)
            // Toast.makeText(context, "${nightId}", Toast.LENGTH_LONG).show()
        })
        binding.sleepList.layoutManager = manager
        binding.sleepList.adapter = adapter

        // by using viewlifecycleowner we can make sure this observer is only around when
        // recyclerview is still on screen
        sleepTrackerViewModel.nights.observe(viewLifecycleOwner, Observer {
            it?.let {
                // adapter.data = it
                adapter.submitList(it)
            }
        })
    }

    private fun setListeners() {
        sleepTrackerViewModel.navigateToSleepQuality.observe(this, Observer { night ->
            night?.let {
                this.findNavController().navigate(
                    SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId)
                )
                sleepTrackerViewModel.doneNavigating()
            }
        })

        sleepTrackerViewModel.showSnackBarEvent.observe(this, Observer {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                sleepTrackerViewModel.doneShowingSnackbar()
            }
        })

        sleepTrackerViewModel.navigateToSleepDataQuality.observe(this, Observer {night ->
            night?.let {
                this.findNavController().navigate(SleepTrackerFragmentDirections
                    .actionSleepTrackerFragmentToSleepDetailFragment(night))
                sleepTrackerViewModel.onSleepDataQualityNavigated()
            }
        })
    }
}

