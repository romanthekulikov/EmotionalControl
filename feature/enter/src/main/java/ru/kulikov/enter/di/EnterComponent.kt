package ru.kulikov.enter.di

import dagger.Component
import ru.kulikov.core.utils.di.CoreComponent
import ru.kulikov.enter.di.modules.EnterModule
import ru.kulikov.enter.ui.EnterActivity
import ru.kulikov.enter.ui.EnterViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [EnterModule::class], dependencies = [CoreComponent::class])
interface EnterComponent {

    @Component.Builder
    interface Builder {
        fun coreComponent(coreComponent: CoreComponent): Builder
        fun build(): EnterComponent
    }

    fun inject(activity: EnterActivity)
    fun inject(viewModel: EnterViewModel)

    companion object {
        private var instance: EnterComponent? = null

        fun init(coreComponent: CoreComponent): EnterComponent {
            if (instance == null) {
                instance = DaggerEnterComponent.builder().coreComponent(coreComponent).build()
            }

            return instance!!
        }

        fun getInstance(): EnterComponent = if (instance != null) instance!! else throw UninitializedPropertyAccessException()
    }
}