package com.example.profile.data

import com.example.profile.domain.ProfileRepository
import com.google.firebase.database.getValue
import com.roman_kulikov.tools.Result
import kotlinx.coroutines.tasks.await
import ru.kulikov.core.utils.data.AppFirebase
import ru.kulikov.core.utils.data.models.UserModel
import ru.kulikov.core.utils.domain.AppSharedPreferences
import ru.kulikov.core.utils.domain.entities.User
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(private val sharedPreferences: AppSharedPreferences) : ProfileRepository {
    override suspend fun getUser(): Result<User> {
        val firebaseUserId = AppFirebase.auth.currentUser!!.uid
        val userId = sharedPreferences.getUserId()
        val name = AppFirebase.usersReference.userReference(firebaseUserId).nameReference().get().await().getValue<String>() ?: ""

        return Result.Success(UserModel(userId, name))
    }

    override suspend fun saveUser(user: User): Result<Boolean> {
        val firebaseUserId = AppFirebase.auth.currentUser!!.uid
        val userReference = AppFirebase.usersReference.userReference(firebaseUserId)
        userReference.nameReference().setValue(user.name)

        return Result.Success(true)
    }
}