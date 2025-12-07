package com.example.main.domain.use_cases.impl

import com.example.main.domain.MainRepository
import com.example.main.domain.use_cases.ForgotPartnerIdUc
import javax.inject.Inject

class ForgotPartnerIdUcImpl @Inject constructor(private val repository: MainRepository) : ForgotPartnerIdUc {
    override fun invoke() {
        repository.forgotPartnerId()
    }
}