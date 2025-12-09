package com.example.profile.ui

import com.roman_kulikov.processor.annotations.FunctionTarget
import com.roman_kulikov.processor.annotations.StateField
import com.roman_kulikov.processor.annotations.UIState
import ru.kulikov.core.utils.data.models.UserModel
import ru.kulikov.core.utils.domain.UserId
import ru.kulikov.core.utils.domain.entities.User

@UIState
data class ProfileState(
    @StateField(FunctionTarget.SET)
    val name: String = "",
    @StateField(FunctionTarget.SET)
    val userId: UserId = -1,
) {
    fun toUser(): User = UserModel(userId, name)
}