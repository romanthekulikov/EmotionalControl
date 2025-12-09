package ru.kulikov.auth.di.modules

import com.roman_kulikov.tools.ExceptionCatcher
import dagger.Binds
import dagger.Module
import ru.kulikov.auth.data.AuthRepositoryImpl
import ru.kulikov.auth.data.exceptions.FirebaseExceptionHandler
import ru.kulikov.auth.domain.AuthRepository
import ru.kulikov.auth.domain.use_cases.AuthUseCase
import ru.kulikov.auth.domain.use_cases.CreateAccountUseCase
import ru.kulikov.auth.domain.use_cases.impl.AuthUseCaseImpl
import ru.kulikov.auth.domain.use_cases.impl.CreateAccountUseCaseImpl
import javax.inject.Singleton

@Module
internal interface AuthModule {

    @Binds
    @Singleton
    fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindAuthUseCase(authUseCaseImpl: AuthUseCaseImpl): AuthUseCase

    @Binds
    @Singleton
    fun bindCreateAccountUseCase(createAccountUseCase: CreateAccountUseCaseImpl): CreateAccountUseCase

    @Binds
    @Singleton
    fun bindExceptionHandler(firebaseExceptionHandler: FirebaseExceptionHandler): ExceptionCatcher
}