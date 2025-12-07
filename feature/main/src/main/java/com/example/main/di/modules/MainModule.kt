package com.example.main.di.modules

import com.example.main.data.MainExceptionCatcher
import com.example.main.data.MainRepositoryImpl
import com.example.main.domain.MainRepository
import com.example.main.domain.use_cases.ForgotPartnerIdUc
import com.example.main.domain.use_cases.GetEmojiUc
import com.example.main.domain.use_cases.GetPartnerIndicatorsUc
import com.example.main.domain.use_cases.GetPartnerNameUc
import com.example.main.domain.use_cases.GetUserIndicatorsUc
import com.example.main.domain.use_cases.GetUserNameUc
import com.example.main.domain.use_cases.SaveIndicatorUc
import com.example.main.domain.use_cases.impl.ForgotPartnerIdUcImpl
import com.example.main.domain.use_cases.impl.GetEmojiUcImpl
import com.example.main.domain.use_cases.impl.GetPartnerIndicatorsUcImpl
import com.example.main.domain.use_cases.impl.GetPartnerNameUcImpl
import com.example.main.domain.use_cases.impl.GetUserIndicatorsUcImpl
import com.example.main.domain.use_cases.impl.GetUserNameUcImpl
import com.example.main.domain.use_cases.impl.SaveIndicatorUcImpl
import com.roman_kulikov.tools.ExceptionCatcher
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

    @Singleton
    @Binds
    fun bindGetEmojiUc(getEmojiUcImpl: GetEmojiUcImpl): GetEmojiUc

    @Singleton
    @Binds
    fun bindForgotPartnerIdUc(forgotPartnerIdUcImpl: ForgotPartnerIdUcImpl): ForgotPartnerIdUc

    @Singleton
    @Binds
    fun bindGetUserNameUc(getUserNameUcImpl: GetUserNameUcImpl): GetUserNameUc

    @Singleton
    @Binds
    fun bindGetPartnerNameUc(getPartnerNameUcImpl: GetPartnerNameUcImpl): GetPartnerNameUc

    @Singleton
    @Binds
    fun bindExceptionCatcher(mainExceptionCatcher: MainExceptionCatcher): ExceptionCatcher
}