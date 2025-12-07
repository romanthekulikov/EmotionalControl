package com.example.main.data

import com.example.main.domain.MainRepository
import com.roman_kulikov.tools.Result
import ru.kulikov.core.utils.data.Indicator
import ru.kulikov.core.utils.domain.AppSharedPreferences
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(sharedPreferences: AppSharedPreferences) : MainRepository {
    override fun getUserIndicators(): Result<List<Indicator>> {
        TODO("Not yet implemented")
    }

    override fun getPartnerIndicators(): Result<List<Indicator>> {
        TODO("Not yet implemented")
    }

    override fun saveIndicator(indicator: Indicator): Result<Boolean> {
        TODO("Not yet implemented")
    }
}