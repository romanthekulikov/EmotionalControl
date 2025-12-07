package com.example.profile.domain.use_cases.impl

import com.example.profile.domain.ProfileRepository
import com.example.profile.domain.use_cases.SaveUserUc
import com.roman_kulikov.tools.Result
import javax.inject.Inject

class SaveUserUcImpl @Inject constructor(private val profileRepository: ProfileRepository) : SaveUserUc {
    override suspend fun invoke(): Result<Boolean> {
        TODO("Not yet implemented")
    }
}