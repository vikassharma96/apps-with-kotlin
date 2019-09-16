package com.teckudos.devappswithkotlin.layouts

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.teckudos.devappswithkotlin.R
import com.teckudos.devappswithkotlin.databinding.ActivityAboutMeBinding

// Data Binding - means taking data and bind it to the view object
// with findViewById getting reference system or android has to search hierarchy for us and find
// view at runtime for deep view it will take time and slow down to avoid this we have technique
// or pattern called dataBinding. which allows us to connect a layout to activity or fragment at
// compile time. compiler creates an helper class called binding class when activity created then
// we can access view through this generated binding class. The big idea about data binding is to
// create an object that connects/maps/binds two pieces of distant information together at compile
// time, so that you don't have to look for it at runtime. The object that surfaces these bindings
// to you is called the Binding object. It is created by the compiler.

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

