package ru.kulikov.statistic.domain

import com.roman_kulikov.tools.Result
import java.time.LocalDate

interface StatisticContract {
    fun partnerStatisticByPeriod(start: LocalDate, end: LocalDate?) : Result<Statistic>
    fun firstRecordDate() : Result<LocalDate>
}