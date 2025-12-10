package ru.kulikov.auth.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.roman_kulikov.tools.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.kulikov.auth.di.AuthComponent
import ru.kulikov.auth.domain.AuthorizeContract
import ru.kulikov.auth.domain.use_cases.AuthUseCase
import ru.kulikov.auth.domain.use_cases.CreateAccountUseCase
import ru.kulikov.core.utils.base.UiEvent
import ru.kulikov.core.utils.launchWithTimeout
import javax.inject.Inject

class AuthViewModel() : AuthStateHandler(), AuthorizeContract {
    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    @Inject
    internal lateinit var authUseCase: AuthUseCase

    @Inject
    internal lateinit var createAccountUseCase: CreateAccountUseCase

    init {
        AuthComponent.getInstance().inject(this)
    }

    override fun auth() {
        viewModelScope.launch {
            withTimeoutProgress {
                handleAuthResult(authUseCase(_state.value.email, _state.value.pass))
            }
        }
    }

    override fun createAccount() {
        viewModelScope.launch {
            withTimeoutProgress {
                handleAuthResult(createAccountUseCase(_state.value.email, _state.value.pass, _state.value.confirmPass))
            }
        }
    }

    private suspend fun handleAuthResult(authResult: Result<FirebaseUser?>) {
        when (authResult) {
            is Result.Failure<*> -> _events.emit(UiEvent.ShowToast(authResult.cause))
            is Result.NoNetwork<*> -> _events.emit(UiEvent.ShowToast("Нет интернета"))
            is Result.Success<*> -> {
                _events.emit(UiEvent.ShowToast("Удачно!"))
                _events.emit(UiEvent.Navigate)
            }
        }
    }

    private suspend fun withTimeoutProgress(job: suspend () -> Unit) {
        _events.emit(UiEvent.InProgress)
        launchWithTimeout(onTimeout) {
            job()
        }
        _events.emit(UiEvent.OutProgress)
    }

    private val onTimeout: suspend (Throwable) -> Unit = {
        Log.e("${this@AuthViewModel::class.simpleName}: ", it.stackTraceToString())
        _events.emit(UiEvent.ShowToast("Internet connection unstable"))
    }

    class Factory() : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel() as T
        }
    }
}