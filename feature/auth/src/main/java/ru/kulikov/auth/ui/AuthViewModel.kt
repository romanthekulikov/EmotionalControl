package ru.kulikov.auth.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kulikov.auth.di.AuthComponent
import ru.kulikov.auth.domain.AuthorizeContract
import ru.kulikov.auth.domain.use_cases.AuthUseCase
import ru.kulikov.auth.domain.use_cases.CreateAccountUseCase
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AuthViewModel : AuthStateHandler(), AuthorizeContract, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    @Inject
    internal lateinit var authUseCase: AuthUseCase

    @Inject
    internal lateinit var createAccountUseCase: CreateAccountUseCase

    init {
        AuthComponent.getInstance().inject(this)
    }

    override fun auth() {
        launch { authUseCase(_state.value.email, _state.value.pass) }
    }

    override fun createAccount() {
        launch { createAccountUseCase(_state.value.email, _state.value.pass, _state.value.confirmPass) }
    }

}