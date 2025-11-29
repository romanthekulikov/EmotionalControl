package ru.kulikov.auth.domain.use_cases

fun interface AuthUseCase {
    suspend operator fun invoke(email: String, pass: String)
}