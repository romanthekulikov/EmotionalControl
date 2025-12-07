package com.example.main.domain

import com.roman_kulikov.tools.Result
import ru.kulikov.core.utils.data.models.Indicator

interface MainRepository {
    suspend fun getUserIndicators(): Result<List<Indicator>>
    suspend fun getPartnerIndicators(): Result<List<Indicator>>
    suspend fun saveIndicator(indicator: Indicator): Result<Boolean>
    fun forgotPartnerId()
    suspend fun getUserName(): Result<String>
    suspend fun getPartnerName(): Result<String>
}