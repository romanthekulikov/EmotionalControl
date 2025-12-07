package ru.kulikov.emotionalcontrol

import android.app.Application
import com.google.firebase.FirebaseApp
import ru.kulikov.auth.di.AuthComponent
import ru.kulikov.core.utils.di.CoreComponent

class EmotionalControlApp : Application() {
    override fun onCreate() {
        initDi()
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

    private fun initDi() {
        val coreComponent = CoreComponent.init(applicationContext)
        AuthComponent.init(coreComponent)
    }
}