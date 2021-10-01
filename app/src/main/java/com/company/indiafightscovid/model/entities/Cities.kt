package com.company.indiafightscovid.model.entities

data class CitiesContainer(
    val id:String,
    val cityData:Cities
        )

data class Cities(
    val todayCases:String?=null,
    val todayRecovered:String?=null,
    val totalCases:String?=null,
    val totalFirstDose:String?=null,
    val totalRecovered:String?=null,
    val totalSecondDose:String?=null,
)