package com.example.main.domain.use_cases

import com.roman_kulikov.tools.Result
import ru.kulikov.core.utils.data.models.Indicator

interface GetPartnerIndicatorsUc {
    suspend operator fun invoke(): Result<List<Indicator>>
}