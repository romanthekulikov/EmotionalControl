package ru.kulikov.auth.data

import com.roman_kulikov.tools.Result
import ru.kulikov.auth.domain.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    override fun auth(email: String, pass: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override fun createAccount(email: String, pass: String): Result<Boolean> {
        TODO("Not yet implemented")
    }
}