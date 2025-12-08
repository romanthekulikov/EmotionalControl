package ru.kulikov.statistic.data

import com.roman_kulikov.tools.Result
import ru.kulikov.core.utils.domain.AppSharedPreferences
import ru.kulikov.statistic.domain.Statistic
import ru.kulikov.statistic.domain.StatisticRepository
import java.time.LocalDate
import javax.inject.Inject

class StatisticRepositoryImpl @Inject constructor(private val sharedPreferences: AppSharedPreferences) :
    StatisticRepository {
    override suspend fun partnerStatisticByPeriod(
        start: LocalDate,
        end: LocalDate?
    ): Result<Statistic> {
        TODO("Not yet implemented")
    }

    override suspend fun firstRecordDate(): Result<LocalDate> {
        TODO("Not yet implemented")
    }
}