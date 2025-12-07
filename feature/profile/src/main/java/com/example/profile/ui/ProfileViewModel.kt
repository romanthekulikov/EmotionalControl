package com.example.profile.ui

import com.example.profile.di.ProfileComponent
import com.example.profile.domain.ProfileContract
import com.example.profile.domain.use_cases.GetUserUc
import com.example.profile.domain.use_cases.SaveUserUc
import javax.inject.Inject

class ProfileViewModel : ProfileStateHandler(), ProfileContract {
    @Inject
    lateinit var getUserUc: GetUserUc

    @Inject
    lateinit var saveUserUc: SaveUserUc

    init {
        ProfileComponent.getInstance().inject(this)
    }


    override fun loadUser() {
        TODO("Not yet implemented")
    }

    override fun setUser() {
        TODO("Not yet implemented")
    }
}