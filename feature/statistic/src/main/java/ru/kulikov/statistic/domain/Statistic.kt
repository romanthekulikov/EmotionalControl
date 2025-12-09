package ru.kulikov.statistic.domain

import ru.kulikov.core.utils.data.Indicator
import java.time.LocalDate

interface Statistic {
    val indicatorValues: List<IndicatorValue>
}

interface IndicatorValue {
    val date: LocalDate
    val indicators: List<Indicator>
}