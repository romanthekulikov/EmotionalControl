package ru.kulikov.enter.domain

interface Room {
    val userId: Int
    val avatarUrl: String
    val name: String

    val indicators: List<IndicatorBase>
    //Todo подумать над индикаторами
}
