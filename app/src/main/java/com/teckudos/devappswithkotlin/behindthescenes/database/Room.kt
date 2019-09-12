package com.teckudos.devappswithkotlin.behindthescenes.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VideoDao {

    @Query("select * from databasevideo")
    fun getVideos(): List<DatabaseVideo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos: DatabaseVideo)
}

