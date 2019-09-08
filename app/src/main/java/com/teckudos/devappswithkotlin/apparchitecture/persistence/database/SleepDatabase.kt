package com.teckudos.devappswithkotlin.apparchitecture.persistence.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SleepNight::class], version = 1, exportSchema = false)
abstract class SleepDatabase : RoomDatabase() {

    abstract val sleepDatabaseDao: SleepDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: SleepDatabase? = null

        fun getInstance(context: Context): SleepDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SleepDatabase::class.java,
                        "sleep_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
//export scheme is true by default it saves schema of database to folder which provide
//version history

//volatile annotation used with variable makes sure value of instance always up to date and same
//to all execution thread the value of volatile variable never be cached and all writes and reads
//to be done with main memory it means changes made by one thread to instance always visible to
//other thread immediately

//wrapping code into synchronized means only one thread of execution at a time can enter this
//block of code which makes sure database initialized only once