package ru.kulikov.auth.data.exceptions

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.roman_kulikov.tools.ExceptionCatcher
import com.roman_kulikov.tools.Result
import javax.inject.Inject

internal class FirebaseExceptionHandler @Inject constructor() : ExceptionCatcher {
    override suspend fun <T> launchWithCatch(job: suspend () -> Result<T>): Result<T> {
        return try {
            job()
        } catch (e: FirebaseAuthUserCollisionException) {
            logException(e)
            Result.Failure("User already exist")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            logException(e)
            Result.Failure("Incorrect email or password")
        } catch (e: FirebaseNetworkException) {
            logException(e)
            Result.NoNetwork()
        } catch (e: FirebaseAuthWeakPasswordException) {
            logException(e)
            Result.Failure("Password isn't protected")
        } catch (e: FirebaseException) {
            logException(e)
            Result.Failure("Error!")
        }
    }

    override fun <T> withCatch(job: () -> Result<T>): Result<T> {
        return try {
            job()
        } catch (e: FirebaseAuthUserCollisionException) {
            logException(e)
            Result.Failure("User already exist")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            logException(e)
            Result.Failure("Incorrect email or password")
        } catch (e: FirebaseNetworkException) {
            logException(e)
            Result.NoNetwork()
        } catch (e: FirebaseAuthWeakPasswordException) {
            logException(e)
            Result.Failure("Password isn't protected")
        } catch (e: FirebaseException) {
            logException(e)
            Result.Failure("Error!")
        }
    }

    private fun logException(e: Throwable) {
        Log.e(TAG, "withCatch:", e)
        //Firebase.crashlytics.recordException(e)
    }

    private companion object {
        val TAG = FirebaseExceptionHandler::class.simpleName
    }
}