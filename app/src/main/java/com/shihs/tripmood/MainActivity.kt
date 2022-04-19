package com.shihs.tripmood

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shihs.tripmood.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Firebase.firestore

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: NavigationBarView  = binding.bottomNavigationView

        navView.menu.getItem(2).isEnabled = false

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_chat,
                R.id.navigation_notifications
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setBtn()
    }

    fun setBtn(){
        binding.addFAB.setOnClickListener {
            navController.navigate(MobileNavigationDirections.actionGlobalPlanModeDialog())
        }
    }

    fun hideActionBar(){
        binding.bottomAppBar.visibility = View.GONE
        binding.addFAB.visibility = View.GONE
        binding.bottomNavigationView.visibility = View.GONE
    }

    fun showActionBar(){
        binding.bottomAppBar.visibility = View.VISIBLE
        binding.addFAB.visibility = View.VISIBLE
        binding.bottomNavigationView.visibility = View.VISIBLE
    }


}