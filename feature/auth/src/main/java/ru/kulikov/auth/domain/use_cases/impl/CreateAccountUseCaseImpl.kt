package ru.kulikov.auth.domain.use_cases.impl

import com.google.firebase.auth.FirebaseUser
import com.roman_kulikov.tools.ExceptionCatcher
import com.roman_kulikov.tools.Result
import ru.kulikov.auth.domain.AuthRepository
import ru.kulikov.auth.domain.use_cases.CreateAccountUseCase
import ru.kulikov.core.utils.isEmail
import javax.inject.Inject

internal class CreateAccountUseCaseImpl @Inject constructor(
    private val exceptionHandler: ExceptionCatcher,
    private val repository: AuthRepository,
) : CreateAccountUseCase {
    override suspend fun invoke(email: String, pass: String, confirmPass: String): Result<FirebaseUser?> {
        if (!isEmail(email)) return Result.Failure("Incorrect email address")
        if (pass != confirmPass) return Result.Failure("Passwords don't match")
        if (!isProtectedPassword(pass)) return Result.Failure("Password isn't protected. Pass len must be > 8, need uppercase and lowercase symbols, digits and special symbols")

        return exceptionHandler.launchWithCatch { repository.createAccount(email, pass) }
    }

    private fun isProtectedPassword(pass: String): Boolean {
        val lengthOk = pass.length >= 8
        val hasUpper = pass.any { it.isUpperCase() }
        val hasLower = pass.any { it.isLowerCase() }
        val hasDigit = pass.any { it.isDigit() }
        val hasSymbol = pass.any { !it.isLetterOrDigit() }

        return lengthOk && hasUpper && hasLower && hasDigit && hasSymbol
    }
}