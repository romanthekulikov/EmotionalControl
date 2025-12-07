package com.example.main.domain.use_cases.impl

import com.example.main.domain.MainRepository
import com.example.main.domain.use_cases.GetUserNameUc
import com.roman_kulikov.tools.ExceptionCatcher
import com.roman_kulikov.tools.Result
import javax.inject.Inject

class GetUserNameUcImpl @Inject constructor(
    private val repository: MainRepository,
    private val exceptionCatcher: ExceptionCatcher,
) : GetUserNameUc {
    override suspend fun invoke(): Result<String> = exceptionCatcher.launchWithCatch { repository.getUserName() }
}