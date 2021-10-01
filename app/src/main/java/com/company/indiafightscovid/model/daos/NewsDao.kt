package com.company.indiafightscovid.model.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.company.indiafightscovid.model.entities.NewsData

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE,)
    suspend fun insertNewsDetails(newsData: List<NewsData>)

    @Query("SELECT * FROM news_data_table ORDER BY ID")
    fun getAllNews():LiveData<List<NewsData>>

    @Query("DELETE FROM news_data_table")
    fun deleteAll()
}