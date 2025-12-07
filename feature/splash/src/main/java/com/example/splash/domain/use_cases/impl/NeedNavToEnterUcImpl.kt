package com.example.splash.domain.use_cases.impl

import com.example.splash.domain.SplashRepository
import com.example.splash.domain.use_cases.NeedNavToEnterUc
import javax.inject.Inject

class NeedNavToEnterUcImpl @Inject constructor(private val repository: SplashRepository) : NeedNavToEnterUc {
    override fun invoke(): Boolean = repository.containsUserId()
}