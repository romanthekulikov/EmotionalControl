package com.example.main.di

import com.example.main.di.modules.MainModule
import com.example.main.ui.MainViewModel
import dagger.Component
import ru.kulikov.core.utils.di.CoreComponent
import javax.inject.Singleton

@Singleton
@Component(modules = [MainModule::class], dependencies = [CoreComponent::class])
interface MainComponent {

    @Component.Builder
    interface Builder {
        fun coreComponent(coreComponent: CoreComponent): Builder
        fun build(): MainComponent
    }

    fun inject(viewModel: MainViewModel)

    companion object {
        private var instance: MainComponent? = null

        fun init(coreComponent: CoreComponent): MainComponent {
            if (instance == null) {

            }

            return instance!!
        }

        fun getInstance(): MainComponent {
            return if (instance != null) instance!! else throw UninitializedPropertyAccessException()
        }
    }
}