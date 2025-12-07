package com.example.profile.data

import com.example.profile.domain.ProfileRepository
import com.roman_kulikov.tools.Result
import ru.kulikov.core.utils.domain.AppSharedPreferences
import ru.kulikov.core.utils.domain.entities.User
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(sharedPreferences: AppSharedPreferences) : ProfileRepository {
    override suspend fun getUser(): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun saveUser(user: User): Result<Boolean> {
        TODO("Not yet implemented")
    }
}