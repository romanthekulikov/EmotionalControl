package com.example.main.domain.use_cases

import com.roman_kulikov.tools.Result

interface GetUserNameUc {
    suspend operator fun invoke(): Result<String>
}