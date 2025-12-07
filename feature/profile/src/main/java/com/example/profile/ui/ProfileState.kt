package com.example.profile.ui

import com.roman_kulikov.processor.annotations.FunctionTarget
import com.roman_kulikov.processor.annotations.StateField
import com.roman_kulikov.processor.annotations.UIState
import ru.kulikov.core.utils.domain.UserId

@UIState
data class ProfileState(
    @StateField(FunctionTarget.SET)
    val name: String = "",
    @StateField(FunctionTarget.SET)
    val userId: UserId = -1,
)