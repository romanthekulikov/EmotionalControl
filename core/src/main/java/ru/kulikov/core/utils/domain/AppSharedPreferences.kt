package ru.kulikov.core.utils.domain

typealias UserId = Int

interface AppSharedPreferences {
    fun saveUserId(userId: UserId)
    fun getUserId(): UserId
    fun containsUserId(): Boolean

    fun savePartnerId(partnerId: UserId)
    fun getPartnerId(): UserId
    fun containsPartnerId(): Boolean

    fun forgotUserId()

    fun forgotPartnerId()
}