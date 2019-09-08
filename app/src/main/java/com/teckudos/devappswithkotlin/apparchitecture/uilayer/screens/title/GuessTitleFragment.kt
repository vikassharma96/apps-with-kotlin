package com.teckudos.devappswithkotlin.apparchitecture.uilayer.screens.title

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.teckudos.devappswithkotlin.R
import com.teckudos.devappswithkotlin.databinding.FragmentGuessTitleBinding

/**
 * Fragment for the starting or title screen of the app
 */
class GuessTitleFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val binding: FragmentGuessTitleBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_guess_title, container, false)

        binding.playGameButton.setOnClickListener {
            findNavController().navigate(GuessTitleFragmentDirections.actionTitleToGame())
        }
        return binding.root
    }
}
