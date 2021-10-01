package com.company.indiafightscovid.model.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.company.indiafightscovid.model.daos.NewsDao
import com.company.indiafightscovid.model.entities.NewsData
import com.company.indiafightscovid.network.NewsDataNetwork
import com.company.indiafightscovid.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsDataRepository(private val newsDao: NewsDao) {

    val news:LiveData<List<NewsData>> = newsDao.getAllNews()

    suspend fun refreshNews(){
        withContext(Dispatchers.IO){
            Log.i("refreshingnews","refresh in progress")
            val newsList=NewsDataNetwork.newsdata.getNewsList()
            newsDao.deleteAll()
            newsDao.insertNewsDetails(newsList.asDatabaseModel())
        }
    }

}