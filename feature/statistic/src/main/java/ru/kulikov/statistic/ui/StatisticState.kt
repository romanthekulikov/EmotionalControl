package ru.kulikov.statistic.ui

import com.roman_kulikov.processor.annotations.FunctionTarget
import com.roman_kulikov.processor.annotations.StateField
import com.roman_kulikov.processor.annotations.UIState
import java.time.LocalDate

@UIState
data class StatisticState(
    @StateField(FunctionTarget.SET)
    val start: LocalDate? = null,
    @StateField(FunctionTarget.SET)
    val end: LocalDate? = null
)
