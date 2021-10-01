package com.company.indiafightscovid.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.company.indiafightscovid.R
import com.company.indiafightscovid.databinding.ActivityLoginBinding
import com.company.indiafightscovid.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient:GoogleSignInClient
    private lateinit var auth:FirebaseAuth
    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    private var signInResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
                val data:Intent?=result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                loginViewModel.handleSignInEvent(task)
        }
        else{
            Toast.makeText(this,"Some Error Occurred\nPlease check your internet connection",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.ThemeNoActionBar)
        mBinding= ActivityLoginBinding.inflate(layoutInflater)
        loginViewModel=ViewModelProvider(this).get(LoginViewModel::class.java)
        setContentView(mBinding.root)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        mBinding.llLoginWithGoogle.setOnClickListener {
//            if(loginViewModel.checkForInternetConnection(this)){
//                signIn()
//            }
//            else{
//                Toast.makeText(this,"Please Check Your Internet Connection",Toast.LENGTH_SHORT).show()
//            }
            signIn()
        }
        loginViewModel.obtainedUser.observe(this){
            goToMainActivity(it)
        }
        loginViewModel.progressBarChecker.observe(this){
            if(it==true){
                mBinding.progressBar.visibility=View.VISIBLE
            }
            else{
                mBinding.progressBar.visibility=View.GONE
            }
        }
        loginViewModel.errorMessage.observe(this){
            if(!it.isNullOrEmpty()){
                Toast.makeText(this,it,Toast.LENGTH_LONG).show()
            }
        }

        auth=Firebase.auth

        // Configure Google Sign In
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }



    override fun onStart() {
        super.onStart()
        goToMainActivity(auth.currentUser)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        signInResultLauncher.launch(signInIntent)
    }
    private fun goToMainActivity(firebaseUser: FirebaseUser?) {
        if(firebaseUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}