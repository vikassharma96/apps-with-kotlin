package com.teckudos.devappswithkotlin.apparchitecture.persistence.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.teckudos.devappswithkotlin.apparchitecture.persistence.database.SleepDatabaseDao
import com.teckudos.devappswithkotlin.apparchitecture.persistence.database.SleepNight
import com.teckudos.devappswithkotlin.apparchitecture.persistence.utils.formatNights
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
    val database: SleepDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var tonight = MutableLiveData<SleepNight?>()

    private val _nights = database.getAllNights()

    val nights: LiveData<List<SleepNight>>
        get() = _nights

    val nightsString = Transformations.map(_nights) { _nights ->
        formatNights(_nights, application.resources)
    }

    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()

    val navigateToSleepQuality: LiveData<SleepNight>
        get() = _navigateToSleepQuality

    fun doneNavigating() {
        _navigateToSleepQuality.value = null
    }

    val startButtonVisible = Transformations.map(tonight) {
        null == it
    }
    val stopButtonVisible = Transformations.map(tonight) {
        null != it
    }
    val clearButtonVisible = Transformations.map(_nights) {
        it?.isNotEmpty()
    }

    private var _showSnackbarEvent = MutableLiveData<Boolean>()

    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        uiScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO) {
            var night = database.getTonight()
            if (night?.endTimeMilli != night?.startTimeMilli) {
                night = null
            }
            night
        }
    }

    fun onStartTracking() {
        uiScope.launch {
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun insert(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.insert(night)
        }
    }

    fun onStopTracking() {
        uiScope.launch {
            val oldNight = tonight.value ?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)
            _navigateToSleepQuality.value = oldNight
        }
    }

    private suspend fun update(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.update(night)
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()
            tonight.value = null
            _showSnackbarEvent.value = true
        }
    }

    suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }
}

// private var viewModelJob = Job - this view model job allows us to cancel all coroutines
// started by this view model when viewmodel is no longer used and destroyed so we don't end up
// coroutines that have no where to return to.

// private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob) scope determine what
// thread the coroutine run on and it also needs to know about the job. to get a scope we need
// instance of  CoroutineScope and pass dispatcher and a job here Dispatchers.Main means
// coroutine launched in ui scope will run on the main thread


/*                          Multithreading and Coroutines                                  */
// Mobile device have processor and these days most of them have multiple hardware processor
// that each run processes concurrently this is called multiprocessing to use processor more
// efficiently the operating system can enable an application to create more than one thread
// of execution within a process this is called multithreading eg think of reading multiple book
// at same time switching between books after each chapter eventually finishing all books
// but we can't read in more than one book at the same time in this case reader represents
// processor and each book represents code than needs to be executed and the act of reading
// each book represent thread of execution.

// scheduler it takes into account things such as priorities and makes sure all thread
// get to run and finish and dispatcher which sets up threads that is it sends your books that
// you need to read and specify the context for that to happen think context as separate
// specialized reading room.

// In android main thread is a single thread that handles all the updates to the UI and main
// thread is also the thread that calls all click handlers and other UI and lifecycle callbacks
// this is why it also called UI thread or default thread meaning unless you explicitly switch
// threads or class runs on different threads everything you do is on main thread.
// UI thread has to runs smoothly for great user experience app to display user without any pauses
// main thread has to update the screens every 16ms or 60 frames per second.

// so to get work done away from main thread a pattern for performing long running tasks without
// blocking the main thread is callbacks by using callbacks we can start long running tasks on
// a background thread when task complete callback supplied as an argument is calls to inform
// of the result on the main thread but they have drawback callback become hard to read and they
// while code look sequential the callback run at some asynchronous in future and callback
// don't allow to use language features such as exceptions

// In kotlin we have coroutines to handle long running tasks elegantly and efficiently.
// in kotlin coroutines lets you convert callback based code to sequential code which make more
// readable and even use language features such as exceptions.
// coroutines are -
// 1.asynchronous 2.non blocking 3.sequential code - use suspend function to make asynchronous
// code sequential - asynchronous means coroutines runs independently rom from the main
// execution steps of the program.
// one of the important aspect of async is that we cannot expect the result is available to us
// until we explicitly wait for it. eg lets say we want an answer of a questions that requires
// some research and we ask a colleague they go off and work on it which is if synchronously
// and on separate thread unless we wait for answer we can continue to do other work that
// doesn't not depend on their answer until they come back and tell you what the answer is.
// Non-blocking means the system is not blocking the main or UI thread. because our coroutine
// code is compiled from sequential code we don't need to specify callbacks and compiler will
// makes sure that the results of coroutines are available before continuing and resuming.
// Suspend is kotlin way of marking a function or function type available to coroutines. when
// a coroutines called a functions marked with suspend instead of blocking untill the function
// returns like normal function calls it suspend executions until the result is ready then it
// resumes where it left off with the results while it suspends waiting for the result it
// unblocks the threads that's it running on so other functions or coroutines can run. suspend
// keyword doesn't specify thread code runs on suspend functions may runs on background thread
// or main thread.
// To use coroutines in kotlin we need
// 1.Job 2.Dispatcher 3.Scope
// Job - a job is anything that can be cancelled all coroutines have a job and we can use this
// to cancel the coroutines. jobs can be arranged in parent child hierarchy so cancellation
// of parent leads to immediate cancellation of all its children.
// Dispatcher - dispatcher sends off coroutines to run on various threads. Dispatcher.MAIN runs
// task on main thread and Dispatcher.IO for offloading blocking of i/o task to shared pool of thread.
// Scope - scope combines information, including a job and dispatcher, to define a context in
// which coroutines runs. scope keeps track of coroutines when we launch a coroutines it's in
// scope which means we said which scope keeps track of coroutines
