package ru.kulikov.enter.domain

interface IndicatorBase {
}

interface EmotionalIndicator : IndicatorBase {
    val emotional: Int
}