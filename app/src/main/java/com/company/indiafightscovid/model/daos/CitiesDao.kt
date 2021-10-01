package com.company.indiafightscovid.model.daos

import android.util.Log
import com.company.indiafightscovid.model.entities.Cities
import com.company.indiafightscovid.model.entities.CitiesContainer
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class CitiesDao {

    private val db=Firebase.firestore

    suspend fun getCitiesDataFromFirestore():ArrayList<CitiesContainer>{
        val citiesList:ArrayList<CitiesContainer> = arrayListOf()
        val cities= db.collection("cities").get().await()
        for (i in cities) {
            val id = i.id
            val city = i.toObject(Cities::class.java)
            val cityListObject = CitiesContainer(id, city)
            citiesList.add(cityListObject)
        }
        Log.d("citiesListData","${citiesList.size}")
        return citiesList
    }

}