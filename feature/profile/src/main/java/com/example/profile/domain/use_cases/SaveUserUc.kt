package com.example.profile.domain.use_cases

import com.roman_kulikov.tools.Result

interface SaveUserUc {
    suspend operator fun invoke(): Result<Boolean>
}