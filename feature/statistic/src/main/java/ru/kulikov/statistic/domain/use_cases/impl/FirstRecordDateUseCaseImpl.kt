package ru.kulikov.statistic.domain.use_cases.impl

import com.roman_kulikov.tools.ExceptionCatcher
import com.roman_kulikov.tools.Result
import ru.kulikov.statistic.domain.StatisticRepository
import ru.kulikov.statistic.domain.use_cases.FirstRecordDateUseCase
import java.time.LocalDate
import javax.inject.Inject

class FirstRecordDateUseCaseImpl @Inject constructor(
    private val repository: StatisticRepository,
    private val exceptionCatcher: ExceptionCatcher,
) : FirstRecordDateUseCase {
    override suspend fun invoke(): Result<LocalDate> {
        TODO("Not yet implemented")
    }
}