package ru.kulikov.enter.ui

import com.roman_kulikov.processor.annotations.FunctionTarget
import com.roman_kulikov.processor.annotations.StateField
import com.roman_kulikov.processor.annotations.UIState

@UIState
data class EnterState(
    @StateField(FunctionTarget.SET)
    val partnerId: Int = 0
)
