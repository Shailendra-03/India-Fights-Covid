package com.company.indiafightscovid.model.daos

import android.util.Log
import com.company.indiafightscovid.model.entities.Suggestions
import com.company.indiafightscovid.model.entities.Users
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class SuggestionsDao {

    private val db=Firebase.firestore
    val suggestionsCollection=db.collection("suggestions")
    private val auth=Firebase.auth
    private val userDao=UserDao()
    private val currentUserId= auth.currentUser!!.uid

    suspend fun addSuggestions(text:String){
        val user=userDao.getUser(currentUserId).await().toObject(Users::class.java)!!
        val currentTime=System.currentTimeMillis()
        val suggestions= Suggestions(text,user,currentTime)
        suggestionsCollection.document().set(suggestions)
    }

    private fun getSuggestionsById(suggestionsId: String): Task<DocumentSnapshot> {
        return suggestionsCollection.document(suggestionsId).get()
    }
    suspend fun updateLikes(suggestionsId:String){
        val suggestion=getSuggestionsById(suggestionsId).await().toObject(Suggestions::class.java)
        val isLiked=suggestion!!.likedBy.contains(currentUserId)
        val isDisliked= suggestion.dislikedBy.contains(currentUserId)
        if(isLiked){
            suggestion.likedBy.remove(currentUserId)
        }else{
            if(isDisliked){
                suggestion.dislikedBy.remove(currentUserId)
                suggestion.likedBy.add(currentUserId)
            }else{
                suggestion.likedBy.add(currentUserId)
            }
        }
        suggestionsCollection.document(suggestionsId).set(suggestion)
    }
    suspend fun updateDislikes(suggestionsId: String){
        val suggestion=getSuggestionsById(suggestionsId).await().toObject(Suggestions::class.java)
        val isLiked=suggestion!!.likedBy.contains(currentUserId)
        val isDisliked= suggestion.dislikedBy.contains(currentUserId)
        if(isDisliked){
            suggestion.dislikedBy.remove(currentUserId)
        }else{
            if(isLiked){
                suggestion.likedBy.remove(currentUserId)
                suggestion.dislikedBy.add(currentUserId)
            }else{
                suggestion.dislikedBy.add(currentUserId)
            }
        }
        suggestionsCollection.document(suggestionsId).set(suggestion)
    }
}