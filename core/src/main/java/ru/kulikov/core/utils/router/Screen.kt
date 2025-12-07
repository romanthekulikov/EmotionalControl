package ru.kulikov.core.utils.router

import android.app.Activity


sealed class Screen {
    data class AuthScreen(val fromActivity: Activity) : Screen()
    data class MainScreen(val fromActivity: Activity) : Screen()
}