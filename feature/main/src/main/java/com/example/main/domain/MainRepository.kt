package com.example.main.domain

import com.roman_kulikov.tools.Result
import ru.kulikov.core.utils.data.Indicator

interface MainRepository {
    suspend fun getUserIndicators(): Result<List<Indicator>>
    suspend fun getPartnerIndicators(): Result<List<Indicator>>
    suspend fun saveIndicator(indicator: Indicator): Result<Boolean>
}