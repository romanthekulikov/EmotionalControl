package ru.kulikov.core.utils.data.models

sealed interface Indicator {
    val name: String
    val percent: Double

    data class EmotionalIndicator(override val name: String = EMOTIONAL_INDICATOR_NAME, override val percent: Double) : Indicator

    companion object {
        const val EMOTIONAL_INDICATOR_NAME = "emotional"
    }
}