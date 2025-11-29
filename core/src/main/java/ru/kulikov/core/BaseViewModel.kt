package ru.kulikov.core

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext = getDispatcher() + getJob() + getExceptionHandler()

    open fun getDispatcher(): CoroutineDispatcher = Dispatchers.IO
    open fun getJob(): Job = SupervisorJob()
    open fun getExceptionHandler(): CoroutineExceptionHandler = CoroutineExceptionHandler { context, throwable ->
        Log.e("CE", throwable.message ?: "Error in coroutine")
    }

}