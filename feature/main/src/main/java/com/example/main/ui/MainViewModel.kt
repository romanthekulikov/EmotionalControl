package com.example.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.main.di.MainComponent
import com.example.main.domain.MainContract
import com.example.main.domain.use_cases.ForgotPartnerIdUc
import com.example.main.domain.use_cases.GetEmojiUc
import com.example.main.domain.use_cases.GetPartnerIndicatorsUc
import com.example.main.domain.use_cases.GetUserIndicatorsUc
import com.example.main.domain.use_cases.SaveIndicatorUc
import com.roman_kulikov.tools.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kulikov.core.utils.base.UiEvent
import ru.kulikov.core.utils.data.models.Indicator
import javax.inject.Inject

class MainViewModel : MainStateHandler(), MainContract {
    @Inject
    lateinit var getUserIndicatorsUc: GetUserIndicatorsUc

    @Inject
    lateinit var getPartnerIndicatorsUc: GetPartnerIndicatorsUc

    @Inject
    lateinit var saveIndicatorUc: SaveIndicatorUc

    @Inject
    lateinit var getEmojiUc: GetEmojiUc

    @Inject
    lateinit var forgotPartnerId: ForgotPartnerIdUc

    private var dataSaved: Boolean = true

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    init {
        MainComponent.getInstance().inject(this)
    }


    override fun getUserIndicators() {
        if (dataSaved) viewModelScope.launch { handleGetUserIndicatorResult(getUserIndicatorsUc()) }
    }

    override fun getPartnerIndicators() {
        if (dataSaved) viewModelScope.launch { handleGetPartnerIndicatorResult(getPartnerIndicatorsUc()) }
    }

    override fun saveUserIndicator() {
        viewModelScope.launch { handleSaveIndicatorResult(saveIndicatorUc(Indicator.EmotionalIndicator(percent = _state.value.userEmotionalIndicator))) }
    }

    override fun forgotPartnerId() {
        forgotPartnerId.invoke()
    }

    override fun setUserEmotionalIndicator(value: Double) {
        if (dataSaved) dataSaved = false
        _state.update { it.copy(userEmotionalIndicator = value, userEmotionalEmoji = getEmojiUc(value)) }
    }

    override fun setPartnerEmotionalIndicator(value: Double) {
        if (dataSaved) dataSaved = false
        _state.update { it.copy(partnerEmotionalIndicator = value, partnerEmotionalEmoji = getEmojiUc(value)) }
    }

    private suspend fun handleGetUserIndicatorResult(result: Result<List<Indicator>>) {
        when (result) {
            is Result.Failure<List<Indicator>> -> _events.emit(UiEvent.ShowToast(result.cause))
            is Result.NoNetwork<List<Indicator>> -> _events.emit(UiEvent.ShowToast("No network"))
            is Result.Success<List<Indicator>> -> {
                result.data.filterIsInstance<Indicator.EmotionalIndicator>().firstOrNull()?.let {
                    setUserEmotionalIndicator(it.percent)
                }
            }
        }
    }

    private suspend fun handleGetPartnerIndicatorResult(result: Result<List<Indicator>>) {
        when (result) {
            is Result.Failure<List<Indicator>> -> _events.emit(UiEvent.ShowToast(result.cause))
            is Result.NoNetwork<List<Indicator>> -> _events.emit(UiEvent.ShowToast("No network"))
            is Result.Success<List<Indicator>> -> {
                result.data.filterIsInstance<Indicator.EmotionalIndicator>().firstOrNull()?.let {
                    setPartnerEmotionalIndicator(it.percent)
                }
            }
        }
    }

    private suspend fun handleSaveIndicatorResult(result: Result<Boolean>) {
        when (result) {
            is Result.Failure<Boolean> -> _events.emit(UiEvent.ShowToast(result.cause))
            is Result.NoNetwork<Boolean> -> _events.emit(UiEvent.ShowToast("No network"))
            is Result.Success<Boolean> -> {
                dataSaved = true
                _events.emit(UiEvent.ShowToast("Success saving"))
            }
        }
    }

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel() as T
        }
    }
}