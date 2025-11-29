package ru.kulikov.auth.domain.use_cases.impl

import ru.kulikov.auth.domain.use_cases.CreateAccountUseCase
import javax.inject.Inject

class CreateAccountUseCaseImpl @Inject constructor() : CreateAccountUseCase {
    override suspend fun invoke(email: String, pass: String, confirmPass: String) {
        TODO("Not yet implemented")
    }
}