package ru.kulikov.enter.domain

import com.roman_kulikov.tools.Result

interface EnterRepository {
    suspend fun enter(partnerId: Int): Result<Boolean>
}