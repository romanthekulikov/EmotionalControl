package com.example.splash.data

import com.example.splash.domain.SplashRepository
import ru.kulikov.core.utils.domain.AppSharedPreferences
import javax.inject.Inject

class SplashRepositoryImpl @Inject constructor(private val sharedPreferences: AppSharedPreferences) : SplashRepository {
    override fun containsUserId(): Boolean = sharedPreferences.containsUserId()
    override fun containsPartnerId(): Boolean = sharedPreferences.containsPartnerId()
}