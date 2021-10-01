package com.company.indiafightscovid.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.company.indiafightscovid.model.daos.NewsDao
import com.company.indiafightscovid.model.daos.UserDao
import com.company.indiafightscovid.model.entities.NewsData
import com.company.indiafightscovid.model.entities.Users

@Database(entities = [NewsData::class],version = 2,exportSchema = false)
abstract class NewsRoomDatabase:RoomDatabase() {
    abstract fun newsDao(): NewsDao

    companion object{

        @Volatile
        private var INSTANCE:NewsRoomDatabase?=null

        fun getDatabase(context: Context):NewsRoomDatabase{

            return INSTANCE?: synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    NewsRoomDatabase::class.java,
                    "news_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE=instance
                instance
            }
        }
    }

}