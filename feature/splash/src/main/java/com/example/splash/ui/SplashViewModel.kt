package com.example.splash.ui

import androidx.lifecycle.ViewModel
import com.example.splash.di.SplashComponent
import com.example.splash.domain.SplashContract
import com.example.splash.domain.use_cases.NeedNavToEnterUc
import com.example.splash.domain.use_cases.NeedNavToMainUc
import javax.inject.Inject

class SplashViewModel : ViewModel(), SplashContract {

    init {
        SplashComponent.getInstance().inject(this)
    }

    @Inject
    lateinit var needNavToEnterUc: NeedNavToEnterUc

    @Inject
    lateinit var needNavToMainUc: NeedNavToMainUc

    override fun needNavigateToEnter(): Boolean = needNavToEnterUc()

    override fun needNavigateToMain(): Boolean = needNavToMainUc()
}