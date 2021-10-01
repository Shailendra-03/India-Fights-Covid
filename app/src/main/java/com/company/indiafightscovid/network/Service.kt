package com.company.indiafightscovid.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface NewsDataService{
    @GET("top-headlines?country=in&category=health&apiKey=14d7a372aad24d56a0f16dbd62826b2b")
    suspend fun getNewsList():NetworkNewsDataContainer
}

object NewsDataNetwork{
    private val retrofit= Retrofit.Builder()
        .baseUrl("https://newsapi.org/v2/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    val newsdata= retrofit.create(NewsDataService::class.java)
}