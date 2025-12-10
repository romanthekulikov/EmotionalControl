package ru.kulikov.statistic.domain.use_cases

import ru.kulikov.statistic.data.PeriodMode
import java.time.LocalDate

interface GetPeriodUseCase {
    operator fun invoke(day: LocalDate, periodMode: PeriodMode): Pair<LocalDate, LocalDate>
}