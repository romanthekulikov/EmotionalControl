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
import javax.inject.Inject

class EnterViewModel : EnterStateHandler(), EnterContract {

    @Inject
    lateinit var enterUseCase: EnterUseCase

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    init {
        EnterComponent.getInstance().inject(this)
    }

    override fun enter() {
        _state.value.partnerId?.let {
            viewModelScope.launch { handleEnterResult(enterUseCase(it)) }
        }
    }

    private suspend fun handleEnterResult(result: Result<Boolean>) {
        when (result) {
            is Result.Failure<*> -> _events.emit(UiEvent.ShowToast(result.cause))
            is Result.NoNetwork<*> -> _events.emit(UiEvent.ShowToast("No internet"))
            is Result.Success<*> -> _events.emit(UiEvent.Navigate)
        }
    }

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EnterViewModel() as T
        }
    }
}