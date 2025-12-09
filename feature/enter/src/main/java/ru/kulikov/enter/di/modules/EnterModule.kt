package ru.kulikov.enter.di.modules

import com.roman_kulikov.tools.ExceptionCatcher
import dagger.Binds
import dagger.Module
import ru.kulikov.enter.data.EnterExceptionCatcher
import ru.kulikov.enter.data.EnterRepositoryImpl
import ru.kulikov.enter.domain.EnterRepository
import ru.kulikov.enter.domain.use_cases.EnterUseCase
import ru.kulikov.enter.domain.use_cases.GetUserIdUseCase
import ru.kulikov.enter.domain.use_cases.impl.EnterUseCaseImpl
import ru.kulikov.enter.domain.use_cases.impl.GetUserIdUseCaseImpl
import javax.inject.Singleton

@Module
interface EnterModule {
    @Singleton
    @Binds
    fun provideExceptionCatcher(exceptionCatcher: EnterExceptionCatcher): ExceptionCatcher

    @Singleton
    @Binds
    fun bindRepository(enterRepositoryImpl: EnterRepositoryImpl): EnterRepository

    @Singleton
    @Binds
    fun bindEnterUseCase(enterUseCaseImpl: EnterUseCaseImpl): EnterUseCase

    @Singleton
    @Binds
    fun bindGetUserIdUseCase(getUserIdUseCaseImpl: GetUserIdUseCaseImpl): GetUserIdUseCase
}