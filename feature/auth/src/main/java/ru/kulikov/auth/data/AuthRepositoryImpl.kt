package ru.kulikov.auth.data

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.getValue
import com.roman_kulikov.tools.Result
import kotlinx.coroutines.tasks.await
import ru.kulikov.auth.domain.AuthRepository
import ru.kulikov.core.utils.data.AppFirebase
import ru.kulikov.core.utils.domain.AppSharedPreferences
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val sharedPreferences: AppSharedPreferences,
) : AuthRepository {

    override suspend fun auth(email: String, pass: String): Result<FirebaseUser?> {
        val result = AppFirebase.auth.signInWithEmailAndPassword(email, pass).await()
        val user = AppFirebase.auth.currentUser
        if (user == null) throw IllegalStateException()

        val userId = AppFirebase.usersReference.userReference(user.uid).idReference().get().await().getValue<Int>()
        if (userId == null) throw IllegalStateException()

        sharedPreferences.saveUserId(userId)
        return Result.Success(result.user)
    }

    override suspend fun createAccount(email: String, pass: String): Result<FirebaseUser?> {
        val result = AppFirebase.auth.createUserWithEmailAndPassword(email, pass).await()
        val user = AppFirebase.auth.currentUser
        if (user == null) throw IllegalStateException()

        generateUserId().let {
            AppFirebase.usersReference.userReference(user.uid).idReference().setValue(it).await()
            sharedPreferences.saveUserId(it)
        }
        return Result.Success(result.user)
    }

    private fun generateUserId(): Int = (100_000..999_999).random()
}