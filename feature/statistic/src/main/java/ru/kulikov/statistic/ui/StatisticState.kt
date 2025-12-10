package ru.kulikov.statistic.ui

import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.formatter.ValueFormatter
import com.roman_kulikov.processor.annotations.FunctionTarget
import com.roman_kulikov.processor.annotations.StateField
import com.roman_kulikov.processor.annotations.UIState
import ru.kulikov.statistic.data.PeriodMode

@UIState
data class StatisticState(
    @StateField(FunctionTarget.SET)
    val mode: PeriodMode = PeriodMode.WEEK,
    @StateField(FunctionTarget.SET)
    val statisticEntries: Pair<BarData?, ValueFormatter?> = Pair(null, null),
)
