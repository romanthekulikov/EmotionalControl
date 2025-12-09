package ru.kulikov.enter.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.roman_kulikov.tools.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.kulikov.core.utils.base.UiEvent
import ru.kulikov.enter.di.EnterComponent
import ru.kulikov.enter.domain.EnterContract
import ru.kulikov.enter.domain.use_cases.EnterUseCase
import ru.kulikov.enter.domain.use_cases.GetUserIdUseCase
import javax.inject.Inject

class EnterViewModel : EnterStateHandler(), EnterContract {

    @Inject
    lateinit var enterUseCase: EnterUseCase

    @Inject
    lateinit var getUserIdUseCase: GetUserIdUseCase

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    init {
        EnterComponent.getInstance().inject(this)
        getUserId()
    }

    override fun enter() {
        viewModelScope.launch { handleEnterResult(enterUseCase(_state.value.partnerId)) }
    }

    override fun getUserId() {
        viewModelScope.launch { handleGetUserId(getUserIdUseCase()) }
    }

    private suspend fun handleEnterResult(result: Result<Boolean>) {
        when (result) {
            is Result.Failure<*> -> _events.emit(UiEvent.ShowToast(result.cause))
            is Result.NoNetwork<*> -> _events.emit(UiEvent.ShowToast("Нет интернета"))
            is Result.Success<*> -> _events.emit(UiEvent.Navigate)
        }
    }

    private suspend fun handleGetUserId(result: Result<Int>) {
        when (result) {
            is Result.Failure<Int> -> _events.emit(UiEvent.ShowToast(result.cause))
            is Result.NoNetwork<Int> -> _events.emit(UiEvent.ShowToast("Нет интернета"))
            is Result.Success<Int> -> setUserId(result.data)
        }
    }

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EnterViewModel() as T
        }
    }
}