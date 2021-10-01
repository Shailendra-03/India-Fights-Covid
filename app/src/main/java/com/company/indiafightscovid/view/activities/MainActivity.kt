package com.company.indiafightscovid.view.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.company.indiafightscovid.R
import com.company.indiafightscovid.databinding.ActivityMainBinding
import com.company.indiafightscovid.model.daos.UserDao
import com.company.indiafightscovid.model.entities.Users
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var userDao: UserDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.bottomNavView
        val drawerNavView=binding.drawerNavView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        userDao= UserDao()
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard,R.id.navigation_suggestion,R.id.navigation_vaccinations,R.id.navigation_about
            ),binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        drawerNavView.setupWithNavController(navController)
        drawerNavView.menu.findItem(R.id.logout).setOnMenuItemClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            true
        }
        lifecycleScope.launch(Dispatchers.IO) {
            val currentUser=userDao.getCurrentUser().await().toObject(Users::class.java)
            withContext(Dispatchers.Main){
                val headerLayoutImage:ImageView=findViewById(R.id.iv_header_layout)
                val headerLayoutTextView:TextView=findViewById(R.id.tv_header_layout)
                Glide.with(this@MainActivity).load(currentUser!!.userImageUrl).error(R.drawable.ic_unknown_person_image).circleCrop().into(headerLayoutImage)
                headerLayoutTextView.text=currentUser.userName
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_activity_main).navigateUp(appBarConfiguration)
    }

}