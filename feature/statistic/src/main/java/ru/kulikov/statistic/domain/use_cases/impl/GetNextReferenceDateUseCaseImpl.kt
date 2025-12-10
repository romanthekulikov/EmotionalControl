package ru.kulikov.statistic.domain.use_cases.impl

import ru.kulikov.statistic.data.PeriodMode
import ru.kulikov.statistic.domain.use_cases.GetNextReferenceDateUseCase
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

class GetNextReferenceDateUseCaseImpl @Inject constructor() : GetNextReferenceDateUseCase {
    override fun invoke(currentReference: LocalDate, periodMode: PeriodMode): LocalDate {
        return when (periodMode) {
            PeriodMode.WEEK -> currentReference.with(TemporalAdjusters.previous(DayOfWeek.MONDAY))
            PeriodMode.MONTH -> currentReference.with(TemporalAdjusters.firstDayOfMonth()).minusMonths(1)
            PeriodMode.YEAR -> currentReference.with(TemporalAdjusters.firstDayOfYear()).minusYears(1)
        }
    }
}