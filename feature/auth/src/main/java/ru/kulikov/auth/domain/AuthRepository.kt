package ru.kulikov.auth.domain

import com.google.firebase.auth.FirebaseUser
import com.roman_kulikov.tools.Result

internal interface AuthRepository {
    suspend fun auth(email: String, pass: String): Result<FirebaseUser?>
    suspend fun createAccount(email: String, pass: String): Result<FirebaseUser?>
}