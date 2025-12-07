package com.example.main.ui

import com.example.main.R
import com.roman_kulikov.processor.annotations.FunctionTarget
import com.roman_kulikov.processor.annotations.StateField
import com.roman_kulikov.processor.annotations.UIState

@UIState
data class MainState(
    @StateField(FunctionTarget.SET)
    val userEmotionalIndicator: Double = 1.0,
    @StateField(FunctionTarget.SET)
    val partnerEmotionalIndicator: Double = 1.0,
    val userEmotionalEmoji: Int = R.drawable.ic_max_emotional,
    val partnerEmotionalEmoji: Int = R.drawable.ic_max_emotional,
)
