package com.company.indiafightscovid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.indiafightscovid.model.daos.UserDao
import com.company.indiafightscovid.model.entities.Users
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class LoginViewModel:ViewModel() {

    private var _progressBarChecker=MutableLiveData(false)
    val progressBarChecker:LiveData<Boolean> get() = _progressBarChecker

    private var _errorMessage=MutableLiveData("")
    val errorMessage:LiveData<String> get() = _errorMessage

    private val userDao= UserDao()

    private var _obtainedUser=MutableLiveData<FirebaseUser>()
    val obtainedUser:LiveData<FirebaseUser> get() = _obtainedUser


    private val auth=Firebase.auth

    fun handleSignInEvent(task: Task<GoogleSignInAccount>?){
        _progressBarChecker.value=true
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task!!.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            _errorMessage.value=e.message
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credentials=GoogleAuthProvider.getCredential(idToken,null)
        viewModelScope.launch(Dispatchers.IO){
            try {
                val authResult=auth.signInWithCredential(credentials).await()
                val firebaseUser= authResult.user!!
                withContext(Dispatchers.Main){
                    addUserToDatabase(firebaseUser)
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    _errorMessage.value=e.message
                    _progressBarChecker.value=false
                }

            }

        }

    }
    private fun addUserToDatabase(firebaseUser: FirebaseUser){
        val user= Users(firebaseUser.photoUrl.toString(),firebaseUser.uid,
            if(firebaseUser.displayName.isNullOrEmpty()){ "Not Available" } else{ firebaseUser.displayName!! }
        )
        viewModelScope.launch(Dispatchers.IO) {
            userDao.addUser(user)
        }
        _progressBarChecker.value=false
        _obtainedUser.value=firebaseUser
    }

//   fun checkForInternetConnection(context: Context): Boolean {
//        val connectionManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
//            val network=connectionManager.activeNetwork ?: return false
//            val activeNetwork=connectionManager.getNetworkCapabilities(network) ?: return false
//            return when {
//                // Indicates this network uses a Wi-Fi transport,
//                // or WiFi has network connectivity
//                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//
//                // Indicates this network uses a Cellular transport. or
//                // Cellular has network connectivity
//                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//
//                // else return false
//                else -> false
//            }
//
//        }
//        else{
//            @Suppress("DEPRECATION")
//            val network=connectionManager.activeNetworkInfo ?:return false
//            @Suppress("DEPRECATION")
//            return network.isConnected
//        }
//
//    }
}