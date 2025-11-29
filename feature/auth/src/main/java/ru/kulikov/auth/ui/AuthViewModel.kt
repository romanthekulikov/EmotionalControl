package ru.kulikov.auth.ui

import kotlinx.coroutines.launch
import ru.kulikov.auth.di.AuthComponent
import ru.kulikov.auth.domain.AuthorizeContract
import ru.kulikov.auth.domain.use_cases.AuthUseCase
import ru.kulikov.auth.domain.use_cases.CreateAccountUseCase
import ru.kulikov.core.BaseViewModel
import javax.inject.Inject

class AuthViewModel : BaseViewModel(), AuthorizeContract {
    @Inject
    lateinit var authUseCase: AuthUseCase

    @Inject
    lateinit var createAccountUseCase: CreateAccountUseCase

    init {
        AuthComponent.getInstance().inject(this)
    }

    override fun auth(email: String, pass: String) {
        launch { authUseCase(email, pass) }
    }

    override fun createAccount(
        email: String,
        pass: String,
        confirmPass: String
    ) {
        launch { createAccountUseCase(email, pass, confirmPass) }
    }

}