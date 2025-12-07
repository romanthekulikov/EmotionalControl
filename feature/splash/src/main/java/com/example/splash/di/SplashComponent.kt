package com.example.splash.di

import com.example.splash.di.modules.SplashModule
import com.example.splash.ui.SplashViewModel
import dagger.Component
import ru.kulikov.core.utils.di.CoreComponent
import javax.inject.Singleton

@Singleton
@Component(modules = [SplashModule::class], dependencies = [CoreComponent::class])
interface SplashComponent {

    @Component.Builder
    interface Builder {
        fun coreComponent(coreComponent: CoreComponent): Builder
        fun build(): SplashComponent
    }

    fun inject(viewModel: SplashViewModel)

    companion object {
        private var instance: SplashComponent? = null

        fun init(coreComponent: CoreComponent): SplashComponent {
            if (instance == null) {
                instance = DaggerSplashComponent.builder().coreComponent(coreComponent).build()
            }

            return instance!!
        }

        fun getInstance(): SplashComponent = if (instance != null) instance!! else throw UninitializedPropertyAccessException()
    }
}