package com.teckudos.devappswithkotlin.apparchitecture.uilayer.screens.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.teckudos.devappswithkotlin.R
import com.teckudos.devappswithkotlin.databinding.FragmentScoreBinding

/**
 * Fragment where the final score is shown, after the game is over
 */
class ScoreFragment : Fragment() {

    private lateinit var binding: FragmentScoreBinding
    private lateinit var viewModel: ScoreViewModel
    private lateinit var viewModelFactory: ScoreViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_score,
            container,
            false
        )
        viewModelFactory =
            ScoreViewModelFactory(
                ScoreFragmentArgs.fromBundle(
                    arguments!!
                ).score
            )
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ScoreViewModel::class.java)

        binding.scoreViewModel = viewModel
        binding.lifecycleOwner = this

        initObservers()

        //binding.scoreText.text = ScoreFragmentArgs.fromBundle(arguments!!).score.toString()

        // Get args using by navArgs property delegate
        //val scoreFragmentArgs by navArgs<ScoreFragmentArgs>()
        //binding.scoreText.text = scoreFragmentArgs.score.toString()

        //binding.playAgainButton.setOnClickListener { viewModel.onPlayAgain() }

        return binding.root
    }

    private fun initObservers() {

        /*viewModel.score.observe(this, Observer { newScore ->
            binding.scoreText.text = newScore.toString()
        })*/

        // Navigates back to title when button is pressed
        viewModel.eventPlayAgain.observe(this, Observer { playAgain ->
            if (playAgain) {
                findNavController().navigate(ScoreFragmentDirections.actionRestart())
                viewModel.onPlayAgainComplete()
            }
        })
    }
}
