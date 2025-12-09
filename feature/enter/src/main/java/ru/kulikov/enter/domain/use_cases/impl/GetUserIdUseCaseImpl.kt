package ru.kulikov.enter.domain.use_cases.impl

import com.roman_kulikov.tools.Result
import ru.kulikov.enter.domain.EnterRepository
import ru.kulikov.enter.domain.use_cases.GetUserIdUseCase
import javax.inject.Inject

class GetUserIdUseCaseImpl @Inject constructor(private val repository: EnterRepository) : GetUserIdUseCase {
    override fun invoke(): Result<Int> = repository.getUserId()
}