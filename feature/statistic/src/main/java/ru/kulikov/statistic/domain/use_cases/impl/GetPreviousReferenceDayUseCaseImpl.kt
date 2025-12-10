package ru.kulikov.statistic.domain.use_cases.impl

import ru.kulikov.statistic.data.PeriodMode
import ru.kulikov.statistic.domain.use_cases.GetPreviousReferenceDayUseCase
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

class GetPreviousReferenceDayUseCaseImpl @Inject constructor() : GetPreviousReferenceDayUseCase {
    override fun invoke(currentReference: LocalDate, periodMode: PeriodMode): LocalDate {
        return when (periodMode) {
            PeriodMode.WEEK -> currentReference.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
            PeriodMode.MONTH -> currentReference.with(TemporalAdjusters.firstDayOfNextMonth())
            PeriodMode.YEAR -> currentReference.with(TemporalAdjusters.firstDayOfNextYear())
        }
    }
}