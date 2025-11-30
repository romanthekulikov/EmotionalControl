package ru.kulikov.auth.domain.use_cases.impl

import com.google.firebase.auth.FirebaseUser
import com.roman_kulikov.tools.ExceptionCatcher
import com.roman_kulikov.tools.Result
import ru.kulikov.auth.domain.AuthRepository
import ru.kulikov.auth.domain.use_cases.AuthUseCase
import ru.kulikov.core.utils.isEmail
import javax.inject.Inject

internal class AuthUseCaseImpl @Inject constructor(
    private val exceptionHandler: ExceptionCatcher,
    private val repository: AuthRepository,
) : AuthUseCase {
    override suspend fun invoke(email: String, pass: String): Result<FirebaseUser?> {
        if (!isEmail(email)) return Result.Failure("Incorrect email address")
        return exceptionHandler.launchWithCatch {
            repository.auth(email, pass)
        }
    }
}