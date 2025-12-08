package ru.kulikov.enter.domain.use_cases

import com.roman_kulikov.tools.Result

interface EnterUseCase {
    suspend operator fun invoke(partnerId: Int): Result<Boolean>
}