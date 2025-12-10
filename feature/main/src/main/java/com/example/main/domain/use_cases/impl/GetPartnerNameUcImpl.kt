package com.example.main.domain.use_cases.impl

import com.example.main.domain.MainRepository
import com.example.main.domain.use_cases.GetPartnerNameUc
import com.roman_kulikov.tools.ExceptionCatcher
import com.roman_kulikov.tools.Result
import javax.inject.Inject

class GetPartnerNameUcImpl @Inject constructor(
    private val repository: MainRepository,
    private val exceptionCatcher: ExceptionCatcher,
) : GetPartnerNameUc {
    override suspend fun invoke(): Result<String> = exceptionCatcher.launchWithCatch { repository.getPartnerName() }
}