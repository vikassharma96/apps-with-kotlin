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


/*
* 1.The first are domain objects, stored in the domain package.
* 2.The second are data transfer objects for the network stored in the network package.
* The third type of object is a database object. These are different from data transfer objects
* and domain objects because they’re entirely for interacting with the database.
*/