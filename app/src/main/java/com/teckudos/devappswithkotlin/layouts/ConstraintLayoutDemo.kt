package com.teckudos.devappswithkotlin.layouts

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.teckudos.devappswithkotlin.R
import kotlinx.android.synthetic.main.activity_constraint_layout_demo.*

class ConstraintLayoutDemo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_layout_demo)

        setListeners()
    }

    private fun setListeners() {
        val clickableViews: List<View> =
            listOf(box_one_text, box_two_text, box_three_text,
                box_four_text, box_five_text, constraint_layout,
                red_button, green_button, yellow_button)

        for (item in clickableViews) {
            item.setOnClickListener { makeColored(it) }
        }
    }

    private fun makeColored(view: View) {
        when (view.id) {

            // Boxes using Color class colors for background
            R.id.box_one_text -> view.setBackgroundColor(Color.DKGRAY)
            R.id.box_two_text -> view.setBackgroundColor(Color.GRAY)

            // Boxes using Android color resources for background
            R.id.box_three_text -> view.setBackgroundResource(android.R.color.holo_green_light)
            R.id.box_four_text -> view.setBackgroundResource(android.R.color.holo_green_dark)
            R.id.box_five_text -> view.setBackgroundResource(android.R.color.holo_green_light)

            // Boxes using custom colors for background
            R.id.red_button -> box_three_text.setBackgroundResource(
                R.color.my_red
            )
            R.id.yellow_button -> box_four_text.setBackgroundResource(
                R.color.my_yellow
            )
            R.id.green_button -> box_five_text.setBackgroundResource(
                R.color.my_green
            )

            else -> view.setBackgroundColor(Color.LTGRAY)
        }
    }
}


/*
* designing custom view
* when we extend class from edittext or textview we don't have to draw as we can simply use there
* override methods but when we want to create our own view from scratch by extending view directly
* we are responsible to draw view each time screen refreshes and overriding view methods that handle
* drawing to properly draw view on screen we have to do -
* 1. Override onSizeChanges() method to calculate view size when it first appears and each time it's
* size changes.
* 2. Override onDraw() method to draw custom view, using a canvas object styled by a paint object
* 2. call invalidate() when responding to a user click that changes how the view is drawn, forcing a
* call to onDraw()to redraw the view. onDraw() called every time screen refreshes. we put less code in
* draw to avoid delay like allocation not done which lead to garbage collection
* canvas and paint class have useful drawing methods like drawText(), setTypeface(), setColor(), drawRect(),
* drawOval(), drawArc() change whether shaped filled,outline,bordered by calling setStyle(), drawBitmaps()
* 4. onMeasure() - to determine view height and width
* for custom view click handle we implement performClick() method in class set isClickable to true in viewClass
* */
