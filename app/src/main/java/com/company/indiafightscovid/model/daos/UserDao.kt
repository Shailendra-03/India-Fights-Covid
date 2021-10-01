package com.company.indiafightscovid.model.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.company.indiafightscovid.model.entities.Users
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserDao {
    val auth=Firebase.auth
    private val db=Firebase.firestore
    private val userCollection=db.collection("users")

    fun addUser(user:Users?){
        user?.let {
            userCollection.document(user.userid).set(it)
        }
    }

    fun getUser(uid:String): Task<DocumentSnapshot> {
        return userCollection.document(uid).get()
    }

    fun getCurrentUser():Task<DocumentSnapshot>{
        return userCollection.document(auth.currentUser!!.uid).get()
    }
}
