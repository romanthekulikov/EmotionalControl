package ru.kulikov.enter.domain.use_cases

import com.roman_kulikov.tools.Result

interface GetUserIdUseCase {
    operator fun invoke(): Result<Int>
}