package com.company.indiafightscovid.model.daos

import com.company.indiafightscovid.model.entities.AllIndiaData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AllIndiaDao {
    private val db=Firebase.firestore
    private val allIndiaCollection=db.collection("AllIndiaData")
    suspend fun getAllIndiaData():DocumentSnapshot{
        return allIndiaCollection.document("AllIndiaData").get().await()
    }

}