package ru.kulikov.emotionalcontrol

import android.app.Application
import com.example.main.di.MainComponent
import com.example.main.ui.MainActivity
import com.example.profile.di.ProfileComponent
import com.example.profile.ui.ProfileActivity
import com.example.splash.di.SplashComponent
import com.google.firebase.FirebaseApp
import ru.kulikov.auth.di.AuthComponent
import ru.kulikov.auth.ui.AuthActivity
import ru.kulikov.core.utils.di.CoreComponent
import ru.kulikov.core.utils.router.Router
import ru.kulikov.core.utils.router.RouterHolder
import ru.kulikov.core.utils.router.Screen
import ru.kulikov.statistic.ui.StatisticActivity
import ru.kulikov.enter.di.EnterComponent
import ru.kulikov.enter.ui.EnterActivity
import ru.kulikov.statistic.di.StatisticComponent

class EmotionalControlApp : Application() {
    override fun onCreate() {
        initDi()
        super.onCreate()
        FirebaseApp.initializeApp(this)
        setRouter()
    }

    private fun initDi() {
        val coreComponent = CoreComponent.init(applicationContext)
        AuthComponent.init(coreComponent)
        SplashComponent.init(coreComponent)
        MainComponent.init(coreComponent)
        EnterComponent.init(coreComponent)
        ProfileComponent.init(coreComponent)
        StatisticComponent.init(coreComponent)
    }

    private fun setRouter() {
        RouterHolder.router = object : Router {
            override fun navigateTo(screen: Screen) {
                when (screen) {
                    is Screen.AuthScreen -> screen.fromActivity.startActivity(AuthActivity.createIntent(screen.fromActivity))
                    is Screen.MainScreen -> screen.fromActivity.startActivity(MainActivity.createIntent(screen.fromActivity))
                    is Screen.StatisticScreen -> screen.fromActivity.startActivity(StatisticActivity.createIntent(screen.fromActivity))
                    is Screen.EnterScreen -> screen.fromActivity.startActivity(EnterActivity.createIntent(screen.fromActivity))
                    is Screen.ProfileScreen -> screen.fromActivity.startActivity(ProfileActivity.createIntent(screen.fromActivity))
                }
            }

        }
    }
}