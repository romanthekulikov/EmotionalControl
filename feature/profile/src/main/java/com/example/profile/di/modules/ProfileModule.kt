package com.example.profile.di.modules

import com.example.profile.data.ProfileRepositoryImpl
import com.example.profile.domain.ProfileRepository
import com.example.profile.domain.use_cases.GetUserUc
import com.example.profile.domain.use_cases.SaveUserUc
import com.example.profile.domain.use_cases.impl.GetUserUcImpl
import com.example.profile.domain.use_cases.impl.SaveUserUcImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ProfileModule {

    @Singleton
    @Binds
    fun bindRepository(profileRepository: ProfileRepositoryImpl): ProfileRepository

    @Singleton
    @Binds
    fun bindGetUserUc(getUserUcImpl: GetUserUcImpl): GetUserUc

    @Singleton
    @Binds
    fun bindSaveUserUc(saveUserUcImpl: SaveUserUcImpl): SaveUserUc
}