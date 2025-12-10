package ru.kulikov.statistic.domain.use_cases.impl

import com.roman_kulikov.tools.ExceptionCatcher
import com.roman_kulikov.tools.Result
import ru.kulikov.statistic.domain.Statistic
import ru.kulikov.statistic.domain.StatisticRepository
import ru.kulikov.statistic.domain.use_cases.GetStatisticByPeriodUseCase
import java.time.LocalDate
import javax.inject.Inject

class GetStatisticByPeriodUseCaseImpl @Inject constructor(
    private val statisticRepository: StatisticRepository,
    private val exceptionCatcher: ExceptionCatcher,
) : GetStatisticByPeriodUseCase {
    override suspend fun invoke(startPeriod: LocalDate, endPeriod: LocalDate): Result<Statistic> {
        return exceptionCatcher.launchWithCatch { statisticRepository.partnerStatisticByPeriod(startPeriod, endPeriod) }
    }
}