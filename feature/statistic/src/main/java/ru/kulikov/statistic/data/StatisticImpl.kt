package ru.kulikov.statistic.data

import ru.kulikov.core.utils.data.models.Indicator
import ru.kulikov.statistic.domain.IndicatorValue
import ru.kulikov.statistic.domain.Statistic
import java.time.LocalDate

data class StatisticImpl(override val indicatorValues: List<IndicatorValue>) : Statistic

data class IndicatorValueImpl(
    override val date: LocalDate,
    override val indicators: List<Indicator>,
) : IndicatorValue