package com.teckudos.devappswithkotlin.apparchitecture.screens.game

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.teckudos.devappswithkotlin.R
import com.teckudos.devappswithkotlin.databinding.FragmentGuessGameBinding
import timber.log.Timber

/**
 * Fragment where the game is played
 */
class GuessGameFragment : Fragment() {

    private lateinit var viewModel: GuessGameViewModel

    private lateinit var binding: FragmentGuessGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_guess_game,
            container,
            false
        )
        binding.gameViewModel = viewModel
        binding.lifecycleOwner = this

        Timber.i("Called ViewModelProviders.of!")
        viewModel = ViewModelProviders.of(this).get(GuessGameViewModel::class.java)

        setListeners()

        return binding.root

    }

    private fun setListeners() {
        /*binding.correctButton.setOnClickListener {
            viewModel.onCorrect()

        }
        binding.skipButton.setOnClickListener {
            viewModel.onSkip()
        }*/

        /*viewModel.word.observe(this, Observer { newWord ->
            binding.wordText.text = newWord
        })*/

        /*viewModel.score.observe(this, Observer { newScore ->
            binding.scoreText.text = newScore.toString()
        })*/

        viewModel.eventGameFinish.observe(this, Observer { hasFinished ->
            if(hasFinished){
                // it called every time and show toast when device rotates because of live data
                // lifecycle awareness behaviour so here fragment destroyed and created so new
                // ui controller observes and get current data which is hasFinished = true
                gameFinished()
                //to prevent it happening
                viewModel.onGameFinishComplete()
            }
        })
        viewModel.currentTime.observe(this, Observer { newTime ->
            binding.timerText.text = DateUtils.formatElapsedTime(newTime)
        })
    }

    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        val action = GuessGameFragmentDirections.actionGameToScore()
        action.score = viewModel.score.value ?: 0
        findNavController(this).navigate(action)
//        Toast.makeText(this.activity, "Game has finished", Toast.LENGTH_SHORT).show()
    }
}
