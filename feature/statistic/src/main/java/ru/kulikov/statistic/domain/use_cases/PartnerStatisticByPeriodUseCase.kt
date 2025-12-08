package ru.kulikov.statistic.domain.use_cases

import com.roman_kulikov.tools.Result
import ru.kulikov.statistic.domain.Statistic
import java.time.LocalDate

interface PartnerStatisticByPeriodUseCase {
    suspend operator fun invoke(start: LocalDate, end: LocalDate?) : Result<Statistic>
}