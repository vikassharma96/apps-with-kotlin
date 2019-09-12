package com.teckudos.devappswithkotlin.behindthescenes.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.teckudos.devappswithkotlin.behindthescenes.domain.Video

@Entity
data class DatabaseVideo constructor(
    @PrimaryKey
    val url: String,
    val updated: String,
    val title: String,
    val description: String,
    val thumbnail: String
)

/*
 * as we want to separate our network or domain object so we use this extension function which converts
 * from database objects to domain objects
 */
fun List<DatabaseVideo>.asDomainModel(): List<Video> {
    return map {
        Video(
            url = it.url,
            title = it.title,
            description = it.description,
            updated = it.updated,
            thumbnail = it.thumbnail
        )
    }
}