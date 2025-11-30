package com.example.splash

fun interface ActivityRouter {
    fun navigateTo(screen: ActivityScreen)
}