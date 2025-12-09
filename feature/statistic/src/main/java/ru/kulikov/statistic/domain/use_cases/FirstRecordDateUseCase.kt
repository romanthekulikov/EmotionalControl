package ru.kulikov.statistic.domain.use_cases

import com.roman_kulikov.tools.Result
import java.time.LocalDate

interface FirstRecordDateUseCase {
    suspend operator fun invoke() : Result<LocalDate>
}