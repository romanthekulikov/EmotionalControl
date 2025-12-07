package com.example.main.domain.use_cases.impl

import com.example.main.domain.MainRepository
import com.example.main.domain.use_cases.GetPartnerIndicatorsUc
import com.roman_kulikov.tools.ExceptionCatcher
import com.roman_kulikov.tools.Result
import ru.kulikov.core.utils.data.Indicator
import javax.inject.Inject

class GetPartnerIndicatorsUcImpl @Inject constructor(
    private val repository: MainRepository,
    private val exceptionCatcher: ExceptionCatcher,
) : GetPartnerIndicatorsUc {
    override suspend fun invoke(): Result<List<Indicator>> {
        return exceptionCatcher.launchWithCatch { repository.getPartnerIndicators() }
    }
}