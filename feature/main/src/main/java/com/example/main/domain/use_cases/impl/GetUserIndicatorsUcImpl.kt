package com.example.main.domain.use_cases.impl

import com.example.main.domain.MainRepository
import com.example.main.domain.use_cases.GetUserIndicatorsUc
import com.roman_kulikov.tools.Result
import ru.kulikov.core.utils.data.Indicator
import javax.inject.Inject

class GetUserIndicatorsUcImpl @Inject constructor(repository: MainRepository) : GetUserIndicatorsUc {
    override suspend fun invoke(): Result<List<Indicator>> {
        TODO("Not yet implemented")
    }
}