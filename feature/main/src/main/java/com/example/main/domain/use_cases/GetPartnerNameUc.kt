package com.example.main.domain.use_cases

import com.roman_kulikov.tools.Result

interface GetPartnerNameUc {
    suspend operator fun invoke(): Result<String>
}