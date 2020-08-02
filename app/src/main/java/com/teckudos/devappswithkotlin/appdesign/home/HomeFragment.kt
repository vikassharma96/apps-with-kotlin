package com.teckudos.devappswithkotlin.appdesign.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.teckudos.devappswithkotlin.R
import com.teckudos.devappswithkotlin.databinding.HomeFragmentBinding
import timber.log.Timber

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = HomeFragmentBinding.inflate(inflater)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.navigateToSearch.observe(viewLifecycleOwner,
            Observer<Boolean> { shouldNavigate ->
                Timber.i("called outside.....")
                if (shouldNavigate == true) {
                    Timber.i("called inside....")
                    val navController = binding.root.findNavController()
                    navController.navigate(R.id.action_homeFragment_to_gdgListFragment)
                    viewModel.onNavigatedToSearch()
                }
            })

        return binding.root
    }
}
