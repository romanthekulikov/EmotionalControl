package ru.kulikov.core.utils.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.kulikov.core.utils.di.modules.CoreModule
import ru.kulikov.core.utils.di.modules.RouterModule
import ru.kulikov.core.utils.domain.AppSharedPreferences
import ru.kulikov.core.utils.router.Router

@Component(modules = [CoreModule::class, RouterModule::class])
interface CoreComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): CoreComponent
    }

    fun provideSharedPreferences(): AppSharedPreferences
    fun provideRouter(): Router

    companion object {
        private var instance: CoreComponent? = null

        fun init(context: Context): CoreComponent {
            if (instance == null) {
                instance = DaggerCoreComponent.builder().context(context).build()
            }

            return instance!!
        }
    }
}