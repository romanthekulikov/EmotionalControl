package ru.kulikov.emotionalcontrol

import android.app.Application
import com.example.splash.ActivityRouter
import com.example.splash.RouterHolder

class EcApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RouterHolder.activityRouter = ActivityRouter { screen ->

        }
    }
}