package com.company.indiafightscovid.model.entities


data class Suggestions(
    val text:String="",
    val user:Users= Users(),
    val createdAt:Long=0,
    val likedBy:ArrayList<String> = arrayListOf(),
    val dislikedBy:ArrayList<String> = arrayListOf()
)