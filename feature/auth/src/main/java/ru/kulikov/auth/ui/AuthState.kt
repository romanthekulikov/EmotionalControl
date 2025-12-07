package ru.kulikov.auth.ui

import com.roman_kulikov.processor.annotations.FunctionTarget
import com.roman_kulikov.processor.annotations.StateField
import com.roman_kulikov.processor.annotations.UIState

@UIState
data class AuthState(
    @StateField(FunctionTarget.SET)
    var isAuth: Boolean = true,
    @StateField(FunctionTarget.SET)
    var email: String = "",
    @StateField(FunctionTarget.SET)
    var pass: String = "",
    @StateField(FunctionTarget.SET)
    var confirmPass: String = "",
)