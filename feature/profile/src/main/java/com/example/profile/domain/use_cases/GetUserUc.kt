package com.example.profile.domain.use_cases

import com.roman_kulikov.tools.Result
import ru.kulikov.core.utils.domain.entities.User

interface GetUserUc {
    suspend operator fun invoke(): Result<User>
}