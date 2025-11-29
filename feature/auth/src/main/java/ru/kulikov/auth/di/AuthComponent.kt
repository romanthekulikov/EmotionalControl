package ru.kulikov.auth.di

import dagger.Component
import ru.kulikov.auth.di.modules.AuthModule
import ru.kulikov.auth.ui.AuthViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [AuthModule::class])
interface AuthComponent {

    fun inject(authViewModel: AuthViewModel)

    companion object {
        private var instance: AuthComponent? = null

        fun init(): AuthComponent {
            if (instance == null) {
                instance = DaggerAuthComponent.builder().build()
            }

            return instance!!
        }

        fun getInstance(): AuthComponent = if (instance != null) instance!! else throw UninitializedPropertyAccessException()
    }
}