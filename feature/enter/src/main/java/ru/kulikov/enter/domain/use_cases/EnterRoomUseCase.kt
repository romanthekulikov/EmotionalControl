package ru.kulikov.enter.domain.use_cases

import com.roman_kulikov.tools.Result
import ru.kulikov.enter.domain.Room

interface EnterRoomUseCase {
    suspend operator fun invoke(partnerId: Int) : Result<Room>
}