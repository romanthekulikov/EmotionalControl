package ru.kulikov.statistic.data

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.database.DatabaseException
import com.roman_kulikov.tools.ExceptionCatcher
import com.roman_kulikov.tools.Result
import javax.inject.Inject

class StatisticExceptionCatcher @Inject constructor() : ExceptionCatcher {
    override suspend fun <T> launchWithCatch(job: suspend () -> Result<T>): Result<T> {
        return try {
            job()
        } catch (e: FirebaseNetworkException) {
            logException(e)
            Result.NoNetwork()
        } catch (e: DatabaseException) {
            logException(e)
            Result.Failure("Произошла ошибка при загрузке данных")
        } catch (e: Exception) {
            logException(e)
            Result.Failure(e.message ?: "Error!!!")
        }
    }

    override fun <T> withCatch(job: () -> Result<T>): Result<T> {
        return try {
            job()
        } catch (e: FirebaseNetworkException) {
            logException(e)
            Result.NoNetwork()
        } catch (e: DatabaseException) {
            logException(e)
            Result.Failure("Произошла ошибка при загрузке данных")
        } catch (e: Exception) {
            logException(e)
            Result.Failure(e.message ?: "Error!!!")
        }
    }

    private fun logException(e: Throwable) {
        Log.e(TAG, "withCatch:", e)
    }

    private companion object {
        val TAG = ExceptionCatcher::class.simpleName
    }
}