package ru.kulikov.core.utils.data

sealed class Indicator(percent: Double) {
    data class EmotionalIndicator(val name: String = "emotional", val percent: Double) : Indicator(percent)
}