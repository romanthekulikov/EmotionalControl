package ru.kulikov.enter.domain.use_cases.impl

import ru.kulikov.enter.domain.Room
import ru.kulikov.enter.domain.use_cases.EnterRoomUseCase
import com.roman_kulikov.tools.Result

class EnterRoomUseCaseImpl : EnterRoomUseCase {
    override suspend fun invoke(partnerId: Int) : Result<Room> {
        throw NotImplementedError()
        TODO("Not yet implemented")
    }
}