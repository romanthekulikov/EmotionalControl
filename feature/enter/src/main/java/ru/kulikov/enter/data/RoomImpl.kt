package ru.kulikov.enter.data

import ru.kulikov.enter.domain.Room

class RoomImpl(
    override val userId: Int,
    override val avatarUrl: String,
    override val name: String,
    override val indicators: List<IndicatorBaseImpl>
) : Room {
}