package ru.kulikov.core.utils

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

suspend inline fun launchWithTimeout(onTimeout: suspend (e: Throwable) -> Unit, timeout: Long = 5000, crossinline job: suspend () -> Unit) {
    try {
        withTimeout(timeout) {
            job()
        }
    } catch (e: TimeoutCancellationException) {
        onTimeout(e)
    }
}