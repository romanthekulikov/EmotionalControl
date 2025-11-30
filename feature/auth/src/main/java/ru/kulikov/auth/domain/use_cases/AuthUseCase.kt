package ru.kulikov.auth.domain.use_cases

import com.google.firebase.auth.FirebaseUser
import com.roman_kulikov.tools.Result

internal fun interface AuthUseCase {
    suspend operator fun invoke(email: String, pass: String): Result<FirebaseUser?>
}