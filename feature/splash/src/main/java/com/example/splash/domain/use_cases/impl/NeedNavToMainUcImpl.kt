package com.example.splash.domain.use_cases.impl

import com.example.splash.domain.SplashRepository
import com.example.splash.domain.use_cases.NeedNavToMainUc
import javax.inject.Inject

class NeedNavToMainUcImpl @Inject constructor(private val repository: SplashRepository) : NeedNavToMainUc {
    override fun invoke(): Boolean = repository.containsUserId() && repository.containsPartnerId()
}