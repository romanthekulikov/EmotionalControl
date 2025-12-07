package com.example.profile.domain.use_cases.impl

import com.example.profile.domain.ProfileRepository
import com.example.profile.domain.use_cases.GetUserUc
import com.roman_kulikov.tools.Result
import ru.kulikov.core.utils.domain.entities.User
import javax.inject.Inject

class GetUserUcImpl @Inject constructor(private val profileRepository: ProfileRepository) : GetUserUc {
    override suspend fun invoke(): Result<User> {
        TODO("Not yet implemented")
    }
}