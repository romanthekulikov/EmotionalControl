package ru.kulikov.statistic.domain

import ru.kulikov.statistic.data.PeriodMode

interface StatisticContract {
    fun loadData()
    fun moveForward()
    fun moveBack()
    fun changeMode(periodMode: PeriodMode)
}