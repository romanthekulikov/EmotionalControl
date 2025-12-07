package com.example.splash.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.splash.R

class SplashAppActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_app)
        if (viewModel.needNavigateToEnter()) {
            // TODO: Navigate to enter screen
        } else if (viewModel.needNavigateToMain()) {
            // TODO: Navigate to main screen
        } else {
            // TODO: Navigate to auth screen
        }
    }
}