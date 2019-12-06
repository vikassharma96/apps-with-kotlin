package com.teckudos.devappswithkotlin.behindthescenes.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.teckudos.devappswithkotlin.behindthescenes.database.getDatabase
import com.teckudos.devappswithkotlin.behindthescenes.repository.VideosRepository
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = VideosRepository(database)

        return try {
            repository.refreshVideos()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}

// previously a lot of different ways to do background thread we had things from jobscheduler, async task,
// threads and handlers, loopers, syncadapter, alarmamanger.
// if the work we have doesn't need to survive process that's we shouldn't need work manager we can go
// with coroutine or thread. eg - let's say we download an image and want to tint our UI based on that color
// we only really need to do it in process we don't really need to wake up app to do that work so only
// used coroutine