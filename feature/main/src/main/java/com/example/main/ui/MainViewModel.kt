package com.example.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.main.di.MainComponent
import com.example.main.domain.MainContract
import com.example.main.domain.use_cases.GetPartnerIndicatorsUc
import com.example.main.domain.use_cases.GetUserIndicatorsUc
import com.example.main.domain.use_cases.SaveIndicatorUc
import ru.kulikov.core.utils.data.Indicator
import javax.inject.Inject

class MainViewModel : MainStateHandler(), MainContract {
    @Inject
    lateinit var getUserIndicatorsUc: GetUserIndicatorsUc

    @Inject
    lateinit var getPartnerIndicatorsUc: GetPartnerIndicatorsUc

    @Inject
    lateinit var saveIndicatorUc: SaveIndicatorUc

    init {
        MainComponent.getInstance().inject(this)
    }


    override fun getUserIndicators(): List<Indicator> {
        TODO("Not yet implemented")
    }

    override fun getPartnerIndicators(): List<Indicator> {
        TODO("Not yet implemented")
    }

    override fun saveUserIndicator(indicator: Indicator) {
        TODO("Not yet implemented")
    }

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel() as T
        }
    }
}