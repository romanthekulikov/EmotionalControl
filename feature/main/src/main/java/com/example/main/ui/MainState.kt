package com.example.main.ui

import com.roman_kulikov.processor.annotations.FunctionTarget
import com.roman_kulikov.processor.annotations.StateField
import com.roman_kulikov.processor.annotations.UIState
import ru.kulikov.core.utils.data.Indicator

@UIState
data class MainState(
    @StateField(FunctionTarget.SET)
    val userIndicators: List<Indicator> = listOf(),
    @StateField(FunctionTarget.SET)
    val partnerIndicators: List<Indicator> = listOf(),
)
