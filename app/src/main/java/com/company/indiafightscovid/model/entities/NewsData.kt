package com.company.indiafightscovid.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "news_data_table")
data class NewsData(
    @ColumnInfo(name = "image_source") val imageSource :String,
    @ColumnInfo val title :String,
    @ColumnInfo val description :String,
    @ColumnInfo val url :String,
    @ColumnInfo(name = "published_at") val publishedAt:String,
    @ColumnInfo(name = "content") val content:String,
    @PrimaryKey(autoGenerate = true) val id :Int = 0

):Serializable
