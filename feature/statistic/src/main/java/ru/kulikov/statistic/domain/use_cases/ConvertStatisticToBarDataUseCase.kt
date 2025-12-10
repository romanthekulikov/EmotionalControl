package ru.kulikov.statistic.domain.use_cases

import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.formatter.ValueFormatter
import ru.kulikov.statistic.data.PeriodMode
import ru.kulikov.statistic.domain.Statistic

interface ConvertStatisticToBarDataUseCase {
    operator fun invoke(statistic: Statistic, periodMode: PeriodMode): Pair<BarData, ValueFormatter>
}