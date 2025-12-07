package com.example.main.domain.use_cases

import com.roman_kulikov.tools.Result

interface SaveIndicatorUc {
    suspend operator fun invoke(): Result<Boolean>
}