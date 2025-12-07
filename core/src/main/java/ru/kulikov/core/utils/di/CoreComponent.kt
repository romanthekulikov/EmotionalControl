package ru.kulikov.core.utils.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.kulikov.core.utils.di.modules.CoreModule
import ru.kulikov.core.utils.domain.AppSharedPreferences

@Component(modules = [CoreModule::class])
interface CoreComponent {

    @Component.Builder
    interface CoreComponentBuilder {
        @BindsInstance
        fun context(context: Context): CoreComponentBuilder
        fun build(): CoreComponent
    }

    fun provideSharedPreferences(): AppSharedPreferences

    companion object {
        private var instance: CoreComponent? = null

        fun init(context: Context): CoreComponent {
            if (instance == null) {
                instance = DaggerCoreComponent.builder().context(context).build()
            }

            return instance!!
        }

        fun getInstance(): CoreComponent = if (instance != null) instance!! else throw UninitializedPropertyAccessException()
    }
}