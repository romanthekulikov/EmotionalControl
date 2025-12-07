package com.example.main.domain.use_cases

import com.roman_kulikov.tools.Result
import ru.kulikov.core.utils.data.Indicator

interface GetUserIndicatorsUc {
    suspend operator fun invoke(): Result<List<Indicator>>
}