package com.example.main.di.modules

import com.example.main.data.MainRepositoryImpl
import com.example.main.domain.MainRepository
import com.example.main.domain.use_cases.GetPartnerIndicatorsUc
import com.example.main.domain.use_cases.GetUserIndicatorsUc
import com.example.main.domain.use_cases.SaveIndicatorUc
import com.example.main.domain.use_cases.impl.GetPartnerIndicatorsUcImpl
import com.example.main.domain.use_cases.impl.GetUserIndicatorsUcImpl
import com.example.main.domain.use_cases.impl.SaveIndicatorUcImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface MainModule {

    @Singleton
    @Binds
    fun bindRepository(mainRepository: MainRepositoryImpl): MainRepository

    @Singleton
    @Binds
    fun bindGetPartnerIndicatorsUc(getPartnerIndicatorsUcImpl: GetPartnerIndicatorsUcImpl): GetPartnerIndicatorsUc

    @Singleton
    @Binds
    fun bindGetUserIndicatorsUc(getUserIndicatorsUcImpl: GetUserIndicatorsUcImpl): GetUserIndicatorsUc

    @Singleton
    @Binds
    fun bindSaveUserUc(saveIndicatorUc: SaveIndicatorUcImpl): SaveIndicatorUc
}