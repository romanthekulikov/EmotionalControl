package com.example.main.domain

import com.roman_kulikov.tools.Result
import ru.kulikov.core.utils.data.Indicator

interface MainRepository {
    fun getUserIndicators(): Result<List<Indicator>>
    fun getPartnerIndicators(): Result<List<Indicator>>
    fun saveIndicator(indicator: Indicator): Result<Boolean>
}