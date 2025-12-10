package ru.kulikov.statistic.domain

import com.roman_kulikov.tools.Result
import java.time.LocalDate

interface StatisticRepository {
    suspend fun partnerStatisticByPeriod(start: LocalDate, end: LocalDate?): Result<Statistic>
}