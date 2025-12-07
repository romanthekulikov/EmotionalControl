package com.example.main.domain.use_cases.impl

import com.example.main.domain.MainRepository
import com.example.main.domain.use_cases.SaveIndicatorUc
import com.roman_kulikov.tools.ExceptionCatcher
import com.roman_kulikov.tools.Result
import ru.kulikov.core.utils.data.Indicator
import javax.inject.Inject

class SaveIndicatorUcImpl @Inject constructor(
    private val repository: MainRepository,
    private val exceptionCatcher: ExceptionCatcher,
) : SaveIndicatorUc {
    override suspend fun invoke(indicator: Indicator): Result<Boolean> {
        return exceptionCatcher.launchWithCatch { repository.saveIndicator(indicator) }
    }
}