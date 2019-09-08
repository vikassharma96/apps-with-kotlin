package com.teckudos.devappswithkotlin.layouts

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.teckudos.devappswithkotlin.R
import com.teckudos.devappswithkotlin.databinding.ActivityAboutMeBinding

class AboutMe : AppCompatActivity() {

    private lateinit var binding: ActivityAboutMeBinding
    private val myName: MyName =
        MyName("Vikas Sharma")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_about_me)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_about_me
        )
        /*findViewById<Button>(R.id.done_button).setOnClickListener {
            addNickname(it)
        }*/
        binding.doneButton.setOnClickListener {
            addNickname(it)
        }
        binding.myName = myName
    }

    private fun addNickname(view: View) {

        binding.apply {
//            nicknameText.text = binding.nicknameEdit.text
            myName?.nickname = nicknameEdit.text.toString()
            invalidateAll() // to refresh ui with new data
            nicknameEdit.visibility = View.GONE
            doneButton.visibility = View.GONE
            nicknameText.visibility = View.VISIBLE
        }

        // Hide the keyboard.
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

