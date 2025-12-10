package com.example.main.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.main.di.MainComponent
import com.example.main.domain.MainContract
import com.example.main.domain.use_cases.ForgotPartnerIdUc
import com.example.main.domain.use_cases.GetEmojiUc
import com.example.main.domain.use_cases.GetPartnerIndicatorsUc
import com.example.main.domain.use_cases.GetPartnerNameUc
import com.example.main.domain.use_cases.GetUserIndicatorsUc
import com.example.main.domain.use_cases.GetUserNameUc
import com.example.main.domain.use_cases.SaveIndicatorUc
import com.roman_kulikov.tools.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kulikov.core.utils.base.UiEvent
import ru.kulikov.core.utils.data.models.Indicator
import ru.kulikov.core.utils.launchWithTimeout
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

    @Inject
    lateinit var getUserNameUc: GetUserNameUc

    @Inject
    lateinit var getPartnerNameUc: GetPartnerNameUc

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    init {
        MainComponent.getInstance().inject(this)
    }

    override fun loadData() {
        viewModelScope.launch {
            withTimeoutProgress {
                getUserIndicators()
                getPartnerIndicators()
                getUserName()
                getPartnerName()
            }
        }
    }

    override fun saveUserIndicator() {
        viewModelScope.launch {
            withTimeoutProgress {
                handleSaveIndicatorResult(saveIndicatorUc(Indicator.EmotionalIndicator(percent = _state.value.userEmotionalIndicator)))
            }
        }
    }

    override fun forgotPartnerId() {
        forgotPartnerId.invoke()
    }

    override fun setUserEmotionalIndicator(value: Double) {
        _state.update { it.copy(userEmotionalIndicator = value, userEmotionalEmoji = getEmojiUc(value)) }
    }

    override fun setPartnerEmotionalIndicator(value: Double) {
        _state.update { it.copy(partnerEmotionalIndicator = value, partnerEmotionalEmoji = getEmojiUc(value)) }
    }

    private suspend fun getUserName() {
        handleGetUserNameResult(getUserNameUc())
    }

    private suspend fun getPartnerName() {
        handleGetPartnerNameResult(getPartnerNameUc())
    }

    private suspend fun getUserIndicators() {
        handleGetUserIndicatorResult(getUserIndicatorsUc())
    }

    private suspend fun getPartnerIndicators() {
        handleGetPartnerIndicatorResult(getPartnerIndicatorsUc())
    }

    private suspend fun handleGetUserIndicatorResult(result: Result<List<Indicator>>) {
        when (result) {
            is Result.Failure<List<Indicator>> -> _events.emit(UiEvent.ShowToast(result.cause))
            is Result.NoNetwork<List<Indicator>> -> _events.emit(UiEvent.ShowToast("Нет интернета"))
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
            is Result.NoNetwork<List<Indicator>> -> _events.emit(UiEvent.ShowToast("Нет интернета"))
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
                _events.emit(UiEvent.ShowToast("Success saving"))
            }
        }
    }

    private suspend fun handleGetUserNameResult(result: Result<String>) {
        when (result) {
            is Result.Failure<String> -> _events.emit(UiEvent.ShowToast(result.cause))
            is Result.NoNetwork<String> -> _events.emit(UiEvent.ShowToast("No network"))
            is Result.Success<String> -> {
                if (result.data.isEmpty()) setUserName("Your emotional indicator") else setUserName(result.data + " emotional indicator")
            }
        }
    }

    private suspend fun handleGetPartnerNameResult(result: Result<String>) {
        when (result) {
            is Result.Failure<String> -> _events.emit(UiEvent.ShowToast(result.cause))
            is Result.NoNetwork<String> -> _events.emit(UiEvent.ShowToast("No network"))
            is Result.Success<String> -> {
                if (result.data.isEmpty()) setPartnerName("Partner emotional indicator") else setPartnerName(result.data + " emotional indicator")
            }
        }
    }

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel() as T
        }
    }

    private suspend fun withTimeoutProgress(job: suspend () -> Unit) {
        _events.emit(UiEvent.InProgress)
        launchWithTimeout(onTimeout) {
            job()
        }
        _events.emit(UiEvent.OutProgress)
    }

    private val onTimeout: suspend (Throwable) -> Unit = {
        Log.e("${this@MainViewModel::class.simpleName}: ", it.stackTraceToString())
        _events.emit(UiEvent.ShowToast("Internet connection unstable"))
    }
}