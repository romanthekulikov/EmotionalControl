package com.example.main.domain

internal interface MainContract {
    fun loadData()
    fun getUserIndicators()
    fun getPartnerIndicators()
    fun saveUserIndicator()
    fun forgotPartnerId()
    fun getUserName()
    fun getPartnerName()
}