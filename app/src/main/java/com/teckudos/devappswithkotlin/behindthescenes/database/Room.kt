package com.teckudos.devappswithkotlin.behindthescenes.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface VideoDao {

    @Query("select * from databasevideo")
    fun getVideos(): LiveData<List<DatabaseVideo>> // when we are using list and do database query it will block main thread but live data do db query in background for us

    @Insert(onConflict = OnConflictStrategy.REPLACE) // OnConflictStrategy tells what to do when conflict occur eg - same primary key
    fun insertAll(vararg videos: DatabaseVideo) // vararg means function takes unknown number of arguments in kotlin
}

@Database(entities = [DatabaseVideo::class], version = 1)
abstract class VideosDatabase : RoomDatabase() {
    abstract val videoDao: VideoDao
}

private lateinit var INSTANCE: VideosDatabase

fun getDatabase(context: Context): VideosDatabase {
    synchronized(VideosDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                VideosDatabase::class.java,
                "videos"
            ).build()
        }
    }
    return INSTANCE
}

// Caching -
// caching means storing something for future use in computer science when we say cache we often mean
// to copy data closer to the place it will be used so it can be accessed faster. Browser for example
// has a cache it stores a copy of every web page we visit on the local file system so we can try to
// load the resources locally then next time you visit the page.
// In android app we cache our data in file system. every android app has access to a folder it can use
// to store cache so we can cache our data just like our browser did with retrofit network library
// every time we get a network result we keep a copy on disk then next time we query the same place
// we load the stored copy from the disk we can configure retrofit to do caching for us.
// Network caching - 1. cache results per query 2.HTTP caching 3.store network result on disk

// cache invalidation - knowing data in cache not correct network request over http has bunch of features
// to get rid of or throw old cache data. we can use shared preference, room, file to store cache
// we use room to add an offline cache.
// when a network result comes in instead of showing right away write it to database then our UI get
// notified about database changes and refresh content on screen. we treat database a single source of
// truth. we always update database and show it to UI.

// scenario - In case when we want to change our username we can do by updating to our cache directly
// then make api call to update it on server this has a major issue as it might be possible that server
// may end up out of sync it may never get the updated data because network not available or server
// experience an error while saving if that happen app will show incorrect data from cache. To avoid
// data out of sync easies way to delay updating cache after saving it on server once we get result
// from server we right it on offline cache.


// previously a lot of different ways to do background thread we had things from jobscheduler, async task,
// threads and handlers, loopers, syncadapter, alarmamanger.
// if the work we have doesn't need to survive process that's we shouldn't need work manager we can go
// with coroutine or thread. eg - let's say we download an image and want to tint our UI based on that color
// we only really need to do it in process we don't really need to wake up app to do that work so only
// used coroutine

