package com.example.animalvision

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        installSplashScreen()

        setContentView(R.layout.activity_main)

        val sharedPreferences =
            getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean("user_logged_in", false)) {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }



    }

}

