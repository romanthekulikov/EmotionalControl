package ru.kulikov.enter.data

import ru.kulikov.enter.domain.EmotionalIndicator
import ru.kulikov.enter.domain.IndicatorBase

class IndicatorBaseImpl : IndicatorBase {
}

class EmotionalIndicatorImpl(override val emotional: Int) : EmotionalIndicator {

}