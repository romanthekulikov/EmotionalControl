package com.example.splash.domain

interface SplashRepository {
    fun containsUserId(): Boolean
    fun containsPartnerId(): Boolean
}