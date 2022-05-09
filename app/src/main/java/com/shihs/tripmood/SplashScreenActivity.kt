package com.shihs.tripmood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import com.shihs.tripmood.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)

        setContentView(binding.root)


        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                var intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
        },3000)



    }
}