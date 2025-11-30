package ru.kulikov.auth.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.roman_kulikov.tools.Result
import kotlinx.coroutines.tasks.await
import ru.kulikov.auth.domain.AuthRepository
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override suspend fun auth(email: String, pass: String): Result<FirebaseUser?> {
        val result = auth.signInWithEmailAndPassword(email, pass).await()
        return Result.Success(result.user)
    }

    override suspend fun createAccount(email: String, pass: String): Result<FirebaseUser?> {
        val result = auth.createUserWithEmailAndPassword(email, pass).await()
        return Result.Success(result.user)
    }
}