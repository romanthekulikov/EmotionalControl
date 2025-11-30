package com.example.splash

import android.content.Context

sealed class ActivityScreen(fromContext: Context) {
    data class Auth(val fromContext: Context) : ActivityScreen(fromContext)
}