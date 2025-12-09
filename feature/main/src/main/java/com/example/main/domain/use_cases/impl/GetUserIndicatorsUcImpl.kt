package com.example.main.domain.use_cases.impl

import com.example.main.domain.MainRepository
import com.example.main.domain.use_cases.GetUserIndicatorsUc
import com.roman_kulikov.tools.ExceptionCatcher
import com.roman_kulikov.tools.Result
import ru.kulikov.core.utils.data.models.Indicator
import javax.inject.Inject

class GetUserIndicatorsUcImpl @Inject constructor(
    private val repository: MainRepository,
    private val exceptionCatcher: ExceptionCatcher,
) : GetUserIndicatorsUc {
    override suspend fun invoke(): Result<List<Indicator>> {
        return exceptionCatcher.launchWithCatch { repository.getUserIndicators() }
    }
}