package com.teckudos.devappswithkotlin.navigation

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.teckudos.devappswithkotlin.R
import com.teckudos.devappswithkotlin.databinding.FragmentTitleBinding

class TitleFragment : Fragment() {

    private lateinit var binding: FragmentTitleBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_title,
            container,
            false
        )
        setHasOptionsMenu(true) // to tell android we have menu associated with title fragment
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        binding.playButton.setOnClickListener { view: View ->

            //          Navigation.findNavController(view).navigate(R.id.action_titleFragment_to_gameFragment)

            /*
            Kotlin Extension function allow a class to extend the functionality of an existing
            class without subclassing it KTX has an extension function for an android view class
            so with kotlin extension we can use
            */

            //view.findNavController().navigate(R.id.action_titleFragment_to_gameFragment)
            //as we are using safe args we will use nav directions everywhere
            view.findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToGameFragment())
        }

        /* Navigation actually create on click listener for us*/

//        binding.playButton.setOnClickListener {
//            Navigation.createNavigateOnClickListener(R.id.action_titleFragment_to_gameFragment)
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, this.findNavController())
                || super.onOptionsItemSelected(item)
    }
}
