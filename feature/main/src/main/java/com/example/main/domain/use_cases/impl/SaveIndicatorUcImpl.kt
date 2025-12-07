package com.example.main.domain.use_cases.impl

import com.example.main.domain.MainRepository
import com.example.main.domain.use_cases.SaveIndicatorUc
import com.roman_kulikov.tools.Result
import javax.inject.Inject

class SaveIndicatorUcImpl @Inject constructor(repository: MainRepository) : SaveIndicatorUc {
    override suspend fun invoke(): Result<Boolean> {
        TODO("Not yet implemented")
    }
}