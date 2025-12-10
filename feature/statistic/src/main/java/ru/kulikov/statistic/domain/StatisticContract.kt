package ru.kulikov.statistic.domain

import ru.kulikov.statistic.data.PeriodMode

interface StatisticContract {
    fun loadData(onDataLoad: () -> Unit = { /* Nothing */ })
    fun moveForward()
    fun moveBack()
    fun changeMode(periodMode: PeriodMode)
}