package com.example.main.domain

internal interface MainContract {
    fun getUserIndicators()
    fun getPartnerIndicators()
    fun saveUserIndicator()
    fun forgotPartnerId()
}