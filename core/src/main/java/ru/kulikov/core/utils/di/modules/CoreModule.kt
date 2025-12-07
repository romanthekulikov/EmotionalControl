package ru.kulikov.core.utils.di.modules

import dagger.Binds
import dagger.Module
import ru.kulikov.core.utils.data.AppSharedPreferencesImpl
import ru.kulikov.core.utils.domain.AppSharedPreferences

@Module
interface CoreModule {

    @Binds
    fun provideSharedPreferences(sharedPreferencesImpl: AppSharedPreferencesImpl): AppSharedPreferences
}