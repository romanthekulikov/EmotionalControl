package ru.kulikov.auth.di

import dagger.Component
import ru.kulikov.auth.di.modules.AuthModule
import ru.kulikov.auth.ui.AuthActivity
import ru.kulikov.auth.ui.AuthViewModel
import ru.kulikov.core.utils.di.CoreComponent
import javax.inject.Singleton

@Singleton
@Component(modules = [AuthModule::class], dependencies = [CoreComponent::class])
interface AuthComponent {

    @Component.Builder
    interface Builder {
        fun coreComponent(coreComponent: CoreComponent): Builder
        fun build(): AuthComponent
    }

    fun inject(activity: AuthActivity)
    fun inject(authViewModel: AuthViewModel)

    companion object {
        private var instance: AuthComponent? = null

        fun init(coreComponent: CoreComponent): AuthComponent {
            if (instance == null) {
                instance = DaggerAuthComponent.builder().coreComponent(coreComponent).build()
            }

            return instance!!
        }

        fun getInstance(): AuthComponent = if (instance != null) instance!! else throw UninitializedPropertyAccessException()
    }
}