package ru.kulikov.auth.domain.use_cases.impl

import ru.kulikov.auth.domain.use_cases.AuthUseCase
import javax.inject.Inject

class AuthUseCaseImpl @Inject constructor() : AuthUseCase {
    override suspend fun invoke(email: String, pass: String) {
        TODO("Not yet implemented")
    }
}