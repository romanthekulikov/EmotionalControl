package ru.kulikov.enter.data

import ru.kulikov.enter.domain.EnterRepository
import javax.inject.Inject

class EnterRepositoryImpl @Inject constructor() : EnterRepository {
    override suspend fun enterRoom(partnerId: Int) : RoomImpl {
        TODO("Not yet implemented")
    }
}