package com.teckudos.devappswithkotlin.connecttonetwork.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teckudos.devappswithkotlin.connecttonetwork.network.MarsApi
import com.teckudos.devappswithkotlin.connecttonetwork.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _status = MutableLiveData<MarsApiStatus>()

    // The external immutable LiveData for the request status String
    val status: LiveData<MarsApiStatus>
        get() = _status

    private val _properties = MutableLiveData<List<MarsProperty>>()

    val properties: LiveData<List<MarsProperty>>
        get() = _properties

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    // as retrofit does all of its work on a background thread for us there's is no reason to use any
    // other thread for our scope and this allows us to easily update the value of our
    // mutablelivedata when we get our result

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties()
    }

    /**
     * enqueue starts network request in background thread
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun getMarsRealEstateProperties() {
        // to use deferred we have to be inside coroutine scope
        coroutineScope.launch {
            // calling getProperties() from MarsApi creates inserts that network call in background thread
            // returning the deferred.
            var getPropertiesDeferred = MarsApi.retrofitService.getProperties()
            try {
                // calling await on the deferred return the result from the network call when the
                // value is ready await is non blocking which means it trigger our api to retrieve
                // data from internet without blocking our current thread.
                _status.value = MarsApiStatus.LOADING
                var listResult = getPropertiesDeferred.await()
                _status.value = MarsApiStatus.DONE
                if (listResult.size > 0) {
                    _properties.value = listResult
                }
                // _status.value = "Success: ${listResult.size} Mars properties retrieved"
            } catch (e: Exception) {
                _status.value = MarsApiStatus.ERROR
                _properties.value = ArrayList()
                // _status.value = "Failure: ${e.message}"
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

enum class MarsApiStatus { LOADING, ERROR, DONE }


// Glide - image have to be downloaded, buffer , decode from the compress format it's in to an image
// that can used by android it should be cached either to-in memory cache or a storage base cache or both
// all has to happen in low priority in background thread so UI remains responsive. we use Glide to
// download, buffer, decode, cache, glide need only two things url and imageview.