package com.example.main.domain

import ru.kulikov.core.utils.data.Indicator

internal interface MainContract {
    fun getUserIndicators(): List<Indicator>
    fun getPartnerIndicators(): List<Indicator>
    fun saveUserIndicator(indicator: Indicator)
}