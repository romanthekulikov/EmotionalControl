package ru.kulikov.enter.domain.use_cases.impl

import ru.kulikov.enter.domain.EnterRepository
import ru.kulikov.enter.domain.use_cases.ForgotUserUseCase
import javax.inject.Inject

class ForgotUserUseCaseImpl @Inject constructor(private val repository: EnterRepository): ForgotUserUseCase {
    override fun invoke() {
        repository.forgotUser()
    }
}