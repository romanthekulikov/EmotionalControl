package ru.kulikov.statistic.domain.use_cases

import ru.kulikov.statistic.data.PeriodMode
import java.time.LocalDate

interface GetNextReferenceDateUseCase {
    operator fun invoke(currentReference: LocalDate, periodMode: PeriodMode): LocalDate
}