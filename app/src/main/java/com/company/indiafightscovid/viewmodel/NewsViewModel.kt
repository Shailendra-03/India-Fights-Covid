package com.company.indiafightscovid.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.company.indiafightscovid.model.entities.NewsData
import com.company.indiafightscovid.model.repository.NewsDataRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.IllegalArgumentException

class NewsViewModel(private val newsDataRepository:NewsDataRepository) :ViewModel() {

    var text=MutableLiveData(0)

    val newsList:LiveData<List<NewsData>> = newsDataRepository.news

    init {
        refreshDataFromRepository()
    }

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                newsDataRepository.refreshNews()
            }catch (e:Exception){
                if(newsList.value.isNullOrEmpty()){
                    Log.i("NetworkRetrofit","Network error${e.message}")
                }
            }
        }
    }

    class Factory(private val repository: NewsDataRepository):ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(NewsViewModel::class.java)){
                return NewsViewModel(repository) as T
            }
            throw IllegalArgumentException("unknown ViewModel class")
        }

    }

}
