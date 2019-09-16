package com.teckudos.devappswithkotlin.lifecycle

import android.os.Handler
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import timber.log.Timber

/**
 * This is a class representing a timer that you can start or stop. The secondsCount outputs a count of
 * how many seconds since it started, every one second.
 *
 * -----
 *
 * Handler and Runnable are beyond the scope of this lesson. This is in part because they deal with
 * threading, which is a complex topic that will be covered in a later lesson.
 *
 * If you want to learn more now, you can take a look on the Android Developer documentation on
 * threading:
 *
 * https://developer.android.com/guide/components/processes-and-threads
 *
 */
class Timer(lifecycle: Lifecycle) : LifecycleObserver {

    // The number of seconds counted since the timer started
    var secondsCount = 0

    /**
     * [Handler] is a class meant to process a queue of messages (known as [android.os.Message]s)
     * or actions (known as [Runnable]s)
     */
    private var handler = Handler()
    private lateinit var runnable: Runnable


    init {
        // Add this as a lifecycle Observer, which allows for the class to react to changes in this
        // activity's lifecycle state
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startTimer() {
        // Create the runnable action, which prints out a log and increments the seconds counter
        runnable = Runnable {
            secondsCount++
            Timber.i("Timer is at : $secondsCount")
            // postDelayed re-adds the action to the queue of actions the Handler is cycling
            // through. The delayMillis param tells the handler to run the runnable in
            // 1 second (1000ms)
            handler.postDelayed(runnable, 1000)
        }

        // This is what initially starts the timer
        handler.postDelayed(runnable, 1000)

        // Note that the Thread the handler runs on is determined by a class called Looper.
        // In this case, no looper is defined, and it defaults to the main or UI thread.
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopTimer() {
        // Removes all pending posts of runnable from the handler's queue, effectively stopping the
        // timer
        handler.removeCallbacks(runnable)
    }
}

// There are two ways to start background thread
// 1. by extending thread class overriding its run method  2. by implementing runnable interface and
// pass this runnable object to new thread we also have android specific classes called handler and
// looper from this we have some higher abstract classes like AsyncTask, HandlerThread, ThreadPoolExecutor
// A Handler allows communicating back with UI thread from other background thread.
// AsyncTask - helps get work on/off the UI thread
// HandlerThread - dedicated thread for API callbacks
// ThreadPool - running lots of parallel work
// IntentService - helps get intents of the UI thread
/*
* Thread -                 run()
*         start() -------------------------> terminate
*                 |task1|task2|task3|task4|...
*                        Message Queue
*                      <---{Looper}--> loops through message queue infinite times
* so we have handler which responsible for getting this packages of work into message queue and execute it
* handler only work for thread that has message queue and looper so we can't instantiate inside run()
* method or background thread without calling Looper.prepare means providing looper
* */