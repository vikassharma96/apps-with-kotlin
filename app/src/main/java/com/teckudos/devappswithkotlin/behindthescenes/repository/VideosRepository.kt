package com.teckudos.devappswithkotlin.behindthescenes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.teckudos.devappswithkotlin.behindthescenes.database.VideosDatabase
import com.teckudos.devappswithkotlin.behindthescenes.database.asDomainModel
import com.teckudos.devappswithkotlin.behindthescenes.domain.Video
import com.teckudos.devappswithkotlin.behindthescenes.network.Network
import com.teckudos.devappswithkotlin.behindthescenes.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideosRepository(private val database: VideosDatabase) {
// we are passing database reference which is simple example of dependency injection so now we don't
// need android context in repository.
// For our repository we split api into two parts one part to load video from offline cache and another
// to refresh the offline cache

    suspend fun refreshVideos() {
        // we are making a database call to save new videos in database. databases on android are stored
        // in file system or disk and in order to save they must perform write reading and writing from
        // disk is called Disk IO it very slow compared to read and write in variable which stored in RAM
        // it addition low level api that database uses are blocking means always block a thread until
        // read write operation is complete because of this we treat Disk IO separately when using coroutine
        // it's important Disk IO runs on IO dispatcher it is dispatcher specially for running task
        // that read and write to disk like datbase operations.
        // withContext forces coroutine to switch to dispatcher specified

        withContext(Dispatchers.IO) {
            val playlist = Network.devbytes.getPlaylist().await()
            // we us await() function to tell the coroutine to suspend until it's available as it executes
            // we can sure that result of getplaylist() is always availble but it doesn't this thread
            // while waiting for it this is because await is a suspend function
            // we can even put this n/w fetch outside of our withcontext since it doesn't block a
            // thread or do disk IO
            database.videoDao.insertAll(*playlist.asDatabaseModel())
        }
    }

    // main interface for videos repository properties that everyone can use to observe video from repository
    val videos: LiveData<List<Video>> = Transformations.map(database.videoDao.getVideos()) {
        it.asDomainModel()
    }

}
// Repository pattern - it provide a unified view of our data from several sources in our app data
// come from network and the database to be combined in the repository. user's of the repository don't
// have to care where the data comes from it can come from network or database or some other source it's
// all hidden away behind the interface provided by the repository.
// repository has one other responsibility it manages the data in cache