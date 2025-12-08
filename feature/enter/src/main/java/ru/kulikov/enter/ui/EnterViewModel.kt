package ru.kulikov.enter.ui

import ru.kulikov.enter.di.EnterComponent
import ru.kulikov.enter.domain.EnterContract
import ru.kulikov.enter.domain.use_cases.EnterUseCase
import javax.inject.Inject

class EnterViewModel : EnterStateHandler(), EnterContract {

    @Inject
    lateinit var enterUseCase: EnterUseCase

    init {
        EnterComponent.getInstance().inject(this)
    }

    override fun enter() {
        TODO("Not yet implemented")
    }
}