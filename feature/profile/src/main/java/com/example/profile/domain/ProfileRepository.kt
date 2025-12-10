package com.example.profile.domain

import com.roman_kulikov.tools.Result
import ru.kulikov.core.utils.domain.entities.User

interface ProfileRepository {
    suspend fun getUser(): Result<User>
    suspend fun saveUser(user: User): Result<Boolean>
}