package com.example.profile.domain.use_cases.impl

import com.example.profile.domain.ProfileRepository
import com.example.profile.domain.use_cases.SaveUserUc
import com.roman_kulikov.tools.ExceptionCatcher
import com.roman_kulikov.tools.Result
import ru.kulikov.core.utils.domain.entities.User
import javax.inject.Inject

class SaveUserUcImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val exceptionCatcher: ExceptionCatcher,
) : SaveUserUc {
    override suspend fun invoke(user: User): Result<Boolean> =
        exceptionCatcher.launchWithCatch { profileRepository.saveUser(user) }
}