package ru.kulikov.statistic.domain.use_cases.impl

import ru.kulikov.statistic.data.PeriodMode
import ru.kulikov.statistic.domain.use_cases.GetPeriodUseCase
import java.time.LocalDate
import javax.inject.Inject

class GetPeriodUseCaseImpl @Inject constructor() : GetPeriodUseCase {
    override fun invoke(day: LocalDate, periodMode: PeriodMode): Pair<LocalDate, LocalDate> {
        return when (periodMode) {
            PeriodMode.WEEK -> {
                val start = day.with(java.time.DayOfWeek.MONDAY)
                val end = day.with(java.time.DayOfWeek.SUNDAY)
                start to end
            }

            PeriodMode.MONTH -> {
                val start = day.withDayOfMonth(1)
                val end = day.withDayOfMonth(day.lengthOfMonth())
                start to end
            }

            PeriodMode.YEAR -> {
                val start = day.withDayOfYear(1)
                val end = day.withDayOfYear(day.lengthOfYear())
                start to end
            }
        }
    }
}