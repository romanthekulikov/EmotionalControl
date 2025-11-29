package ru.kulikov.auth.domain.use_cases

fun interface CreateAccountUseCase {
    suspend operator fun invoke(email: String, pass: String, confirmPass: String)
}