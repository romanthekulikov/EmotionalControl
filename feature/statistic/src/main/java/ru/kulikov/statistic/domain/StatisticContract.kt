package ru.kulikov.statistic.domain

import com.roman_kulikov.tools.Result
import java.time.LocalDate

interface StatisticContract {
    fun partnerStatisticByPeriod() : Result<Statistic>
    fun firstRecordDate() : Result<LocalDate>
}