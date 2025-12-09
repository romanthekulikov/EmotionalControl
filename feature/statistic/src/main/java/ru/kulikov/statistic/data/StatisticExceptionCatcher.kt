package ru.kulikov.statistic.data

import com.roman_kulikov.tools.ExceptionCatcher
import com.roman_kulikov.tools.Result
import javax.inject.Inject

class StatisticExceptionCatcher @Inject constructor() : ExceptionCatcher {
    override suspend fun <T> launchWithCatch(job: suspend () -> Result<T>): Result<T> {
        TODO("Not yet implemented")
    }

    override fun <T> withCatch(job: () -> Result<T>): Result<T> {
        TODO("Not yet implemented")
    }
}