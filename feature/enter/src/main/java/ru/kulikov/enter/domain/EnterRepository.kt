package ru.kulikov.enter.domain

internal interface EnterRepository {
    suspend fun enterRoom(partnerId: Int) : Room
}