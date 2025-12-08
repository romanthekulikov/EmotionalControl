package ru.kulikov.enter.domain.use_cases.impl

import com.roman_kulikov.tools.ExceptionCatcher
import com.roman_kulikov.tools.Result
import ru.kulikov.enter.domain.EnterRepository
import ru.kulikov.enter.domain.use_cases.EnterUseCase
import javax.inject.Inject

class EnterUseCaseImpl @Inject constructor(
    private val repository: EnterRepository,
    private val exceptionCatcher: ExceptionCatcher,
) : EnterUseCase {
    override suspend fun invoke(partnerId: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }
}