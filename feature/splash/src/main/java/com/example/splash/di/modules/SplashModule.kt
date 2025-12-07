package com.example.splash.di.modules

import com.example.splash.data.SplashRepositoryImpl
import com.example.splash.domain.SplashRepository
import com.example.splash.domain.use_cases.NeedNavToEnterUc
import com.example.splash.domain.use_cases.NeedNavToMainUc
import com.example.splash.domain.use_cases.impl.NeedNavToEnterUcImpl
import com.example.splash.domain.use_cases.impl.NeedNavToMainUcImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface SplashModule {

    @Singleton
    @Binds
    fun bindNeedNavToMainUc(needNavToMainUc: NeedNavToMainUcImpl): NeedNavToMainUc

    @Singleton
    @Binds
    fun bindNeedNavToEnterUc(needNavToEnterUc: NeedNavToEnterUcImpl): NeedNavToEnterUc

    @Singleton
    @Binds
    fun bindRepository(repositoryImpl: SplashRepositoryImpl): SplashRepository
}