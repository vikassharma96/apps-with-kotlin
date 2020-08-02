package com.teckudos.devappswithkotlin.navigation

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.teckudos.devappswithkotlin.R
import com.teckudos.devappswithkotlin.databinding.FragmentGameWonBinding


class GameWonFragment : Fragment() {

    private lateinit var args: GameWonFragmentArgs
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentGameWonBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_game_won, container, false
        )

        args = GameWonFragmentArgs.fromBundle(arguments!!)

        binding.nextMatchButton.setOnClickListener { view ->
            //view.findNavController().navigate(R.id.action_gameWonFragment_to_gameFragment)
            //as we are using safe args we will use nav directions everywhere
            view.findNavController()
                .navigate(GameWonFragmentDirections.actionGameWonFragmentToGameFragment())
        }
        Toast.makeText(
            context,
            "NumCorrect: ${args.numCorrect}, NumQuestions: ${args.numQuestions}",
            Toast.LENGTH_LONG
        ).show()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.winner_menu, menu)
        //check if activity can resolve our intent if not then we hide share menu
        if (null == getSharedIntent().resolveActivity(activity!!.packageManager)) {
            menu.findItem(R.id.share).isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> shareSuccess()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getSharedIntent(): Intent {
        /*val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            getString(R.string.share_success_text, args.numCorrect, args.numQuestions)
        )
        return shareIntent*/
        /*using shareCompat we can directly generate this intent it has fluent api */
        return ShareCompat.IntentBuilder.from(activity)
            .setText(getString(R.string.share_success_text, args.numCorrect, args.numQuestions))
            .setType("text/plain")
            .intent
    }

    private fun shareSuccess() {
        startActivity(getSharedIntent())
    }
}
