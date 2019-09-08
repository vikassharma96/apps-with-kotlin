package com.teckudos.devappswithkotlin.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.teckudos.devappswithkotlin.R
import com.teckudos.devappswithkotlin.databinding.FragmentGameOverBinding

class GameOverFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentGameOverBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_game_over, container, false)
        binding.tryAgainButton.setOnClickListener {
            //it.findNavController().navigate(R.id.action_gameOverFragment_to_gameFragment)
            //as we are using safe args we will use nav directions everywhere
            it.findNavController().navigate(GameOverFragmentDirections.actionGameOverFragmentToGameFragment())
        }
        return binding.root
    }
}
