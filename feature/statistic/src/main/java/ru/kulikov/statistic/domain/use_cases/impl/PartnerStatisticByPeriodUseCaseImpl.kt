package ru.kulikov.statistic.domain.use_cases.impl

import com.roman_kulikov.tools.ExceptionCatcher
import com.roman_kulikov.tools.Result
import ru.kulikov.statistic.domain.Statistic
import ru.kulikov.statistic.domain.StatisticRepository
import ru.kulikov.statistic.domain.use_cases.PartnerStatisticByPeriodUseCase
import java.time.LocalDate
import javax.inject.Inject

class PartnerStatisticByPeriodUseCaseImpl @Inject constructor(
    private val repository: StatisticRepository,
    private val exceptionCatcher: ExceptionCatcher,
) : PartnerStatisticByPeriodUseCase {
    override suspend fun invoke(
        start: LocalDate,
        end: LocalDate?
    ): Result<Statistic> {
        TODO("Not yet implemented")
    }
}