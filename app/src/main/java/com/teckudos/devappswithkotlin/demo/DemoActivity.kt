package com.teckudos.devappswithkotlin.demo

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.teckudos.devappswithkotlin.R
import com.teckudos.devappswithkotlin.databinding.ActivityDemoBinding
import java.util.*


class DemoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDemoBinding
    private lateinit var pendingIntent: PendingIntent
    private lateinit var alarmManager: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_demo)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_demo)
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    fun onToggleClicked(view: View) {
        var time: Long
        if ((view as ToggleButton).isChecked) {
            Toast.makeText(this, "ALARM ON", Toast.LENGTH_SHORT).show()
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, binding.timePicker.currentHour)
            calendar.set(Calendar.MINUTE, binding.timePicker.currentMinute)
            val intent = Intent(this, AlarmReceiver::class.java)
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
            time = calendar.timeInMillis - calendar.timeInMillis % 60000
            if (System.currentTimeMillis() > time) {
                time =
                    if (Calendar.AM_PM === 0) time + 1000 * 60 * 60 * 12 else time + 1000 * 60 * 60 * 24
            }
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent)
        } else {
            alarmManager.cancel(pendingIntent)
            Toast.makeText(this, "ALARM OFF", Toast.LENGTH_SHORT).show()
        }
        val d = D("hi", 0)
        d.component1()
        d.component2()
        val d2 = d.copy(s = "hello")
        val (name, age) = d2
    }
}

data class D(val s: String, val i: Int) : dd {
    override fun print() {
        // need to implement
    }
}

interface dd{
    fun print(): Unit
}

/*
* ComponentN in data class
* data classes automatically declare componentN() functions
* data class D(val s: String, val s1: String)
* val d = D("hi", "")
* d.component1()
* d.component2()
* val d2 = d.copy(s = "hello")
* val (name) = d2 // destructive declaration
* */

/*
* :: is used for reflection in kotlin
* Class References: The most basic reflection feature is getting the runtime reference to a Kotlin class.
* To obtain the reference to a statically known Kotlin class, you can use the class literal syntax:
* 1.Class Reference val myClass = MyClass::class
* 2.Function Reference list::isEmpty()
* 3.Property Reference ::someVal.isInitialized
* 4.Constructor Reference ::MyClass
*
* :: converts kotlin function to lambda
*
* class Demo(var someOtherVar:Any,var printSquare:(Int) -> Unit){
*           fun doTheSquare(i:Int){
*               printSquare(i)
*           }
* }
* fun printSquare(a: Int) = println(a * 2)
* creating object Demo("creating class object", printSquare) // not lambda printSquare compile error of wrong argument
* to resolve this we use Demo("creating class object", ::printSquare)
* */

/*
* androidtest folder - Expresso test UITest
* @Test fun testing() {
*        val activityScenerio = ActivityScenerio.launch(DemoActivity::class.java)
*        onView(withId(R.id.demo_title)
*              .check(matches(WithText(R.string.demo_text)))
* }
* it run alphabetically
* test folder - Junit4 test
*
*
* */

