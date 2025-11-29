package ru.kulikov.auth.domain

import com.roman_kulikov.tools.Result

interface AuthRepository {
    fun auth(email: String, pass: String): Result<Boolean>
    fun createAccount(email: String, pass: String): Result<Boolean>
}