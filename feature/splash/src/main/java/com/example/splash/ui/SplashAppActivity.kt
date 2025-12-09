package com.example.splash.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.splash.R
import com.example.splash.di.SplashComponent
import ru.kulikov.core.utils.router.Router
import ru.kulikov.core.utils.router.Screen
import javax.inject.Inject

class SplashAppActivity : AppCompatActivity() {

    @Inject
    lateinit var router: Router

    private val viewModel: SplashViewModel by viewModels()

    init {
        SplashComponent.getInstance().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_app)

        if (viewModel.needNavigateToMain()) {
            router.navigateTo(Screen.MainScreen(this))
        } else if (viewModel.needNavigateToEnter()) {
            router.navigateTo(Screen.EnterScreen(this))
        } else {
            router.navigateTo(Screen.AuthScreen(this))
        }
        finish()
    }
}