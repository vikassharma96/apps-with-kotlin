package com.teckudos.devappswithkotlin.apparchitecture.uilayer.screens.game

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
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

        Timber.i("Called ViewModelProviders.of!")
        viewModel = ViewModelProviders.of(this).get(GuessGameViewModel::class.java)

        binding.gameViewModel = viewModel
        binding.lifecycleOwner = this

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
            if (hasFinished) {
                // it called every time and show toast when device rotates because of live data
                // lifecycle awareness behaviour so here fragment destroyed and created so new
                // ui controller observes and get current data which is hasFinished = true
                gameFinished()
                //to prevent it happening
                viewModel.onGameFinishComplete()
            }
        })
        /*viewModel.currentTime.observe(this, Observer { newTime ->
            binding.timerText.text = DateUtils.formatElapsedTime(newTime)
            //DateUtils.formatElapsedTime(newTime) this formatting belongs to view model
            //to do simple manipulation to live data such as changing integer to string we use
            //method called transformation.map so we have live data A we apply transformations
            //and then result to live data B which we observer
            //for complex transformation we have switch map or mediator live data
        })*/

        //Buzzes when triggered with different buzz events
        viewModel.eventBuzz.observe(this, Observer { buzzType ->
            if (buzzType != GuessGameViewModel.BuzzType.NO_BUZZ) {
                buzz(buzzType.pattern)
                viewModel.onBuzzComplete()
            }
        })
    }

    /**
     * Given a pattern, this method makes sure the device buzzes
     */
    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()
        buzzer?.let {
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }

    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        val action =
            GuessGameFragmentDirections.actionGameToScore()
        action.score = viewModel.score.value ?: 0
        findNavController(this).navigate(action)
        //Toast.makeText(this.activity, "Game has finished", Toast.LENGTH_SHORT).show()
    }
}
