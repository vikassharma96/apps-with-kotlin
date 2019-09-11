package com.teckudos.devappswithkotlin.connecttonetwork.detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.teckudos.devappswithkotlin.databinding.FragmentDetailBinding

/**
 * This [Fragment] will show the detailed information about a selected piece of Mars real estate.
 */
class DetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        @Suppress("UNUSED_VARIABLE")
        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        return binding.root
    }
}