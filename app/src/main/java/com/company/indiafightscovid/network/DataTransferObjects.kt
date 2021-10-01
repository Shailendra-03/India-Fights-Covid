package com.company.indiafightscovid.network

import com.company.indiafightscovid.model.entities.NewsData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true,)
data class NetworkNewsDataContainer(@field:Json(name = "articles")val news:List<NetworkNewsData>)

@JsonClass(generateAdapter = true)
data class NetworkNewsData(
    @field:Json(name = "title")val title:String?,
    @field:Json(name = "description")val description:String?,
    @field:Json(name = "url")val url:String?,
    @field:Json(name = "urlToImage")val urlToImage:String?,
    @field:Json(name = "publishedAt")val publishedAt:String?,
    @field:Json(name = "content")val content:String?
)

fun NetworkNewsDataContainer.asDatabaseModel():List<NewsData>{
    return news.map {
        NewsData(
            imageSource = if(it.urlToImage.isNullOrEmpty()){
                "Not Available"
            }else{
                it.urlToImage
                 },
            title = if(it.title.isNullOrEmpty()){
                "Not Available"
            }else{
                it.title
            },
            description=if(it.description.isNullOrEmpty()){
                "Not Available"
            }else{
                it.description
            },
            url = if(it.url.isNullOrEmpty()){
                "Not Available"
            }else{
                it.url
                 },
            publishedAt = if (it.publishedAt.isNullOrEmpty()){
                "Not Available"
            }else{
                it.publishedAt
            },
            content = if(it.content.isNullOrEmpty()){
                "Not Available"
            }else{
                it.content
            }
        )
    }
}