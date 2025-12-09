package ru.kulikov.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import com.roman_kulikov.tools.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.kulikov.auth.di.AuthComponent
import ru.kulikov.auth.domain.AuthorizeContract
import ru.kulikov.auth.domain.use_cases.AuthUseCase
import ru.kulikov.auth.domain.use_cases.CreateAccountUseCase
import ru.kulikov.core.utils.base.UiEvent
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AuthViewModel() : AuthStateHandler(), AuthorizeContract, CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.IO
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
        launch { handleAuthResult(authUseCase(_state.value.email, _state.value.pass)) }
    }

    override fun createAccount() {
        launch { handleAuthResult(createAccountUseCase(_state.value.email, _state.value.pass, _state.value.confirmPass)) }
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

    class Factory() : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel() as T
        }
    }
}