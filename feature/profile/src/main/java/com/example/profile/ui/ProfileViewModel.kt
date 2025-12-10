package com.example.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.profile.di.ProfileComponent
import com.example.profile.domain.ProfileContract
import com.example.profile.domain.use_cases.GetUserUc
import com.example.profile.domain.use_cases.SaveUserUc
import com.roman_kulikov.tools.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.kulikov.core.utils.base.UiEvent
import ru.kulikov.core.utils.domain.entities.User
import javax.inject.Inject

class ProfileViewModel : ProfileStateHandler(), ProfileContract {
    @Inject
    lateinit var getUserUc: GetUserUc

    @Inject
    lateinit var saveUserUc: SaveUserUc

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    init {
        ProfileComponent.getInstance().inject(this)
        loadUser()
    }


    override fun loadUser() {
        viewModelScope.launch { handleGetUserResult(getUserUc()) }
    }

    override fun setUser() {
        viewModelScope.launch { handleUserSaveResult(saveUserUc(_state.value.toUser())) }
    }

    private suspend fun handleGetUserResult(result: Result<User>) {
        when (result) {
            is Result.Failure<User> -> _events.emit(UiEvent.ShowToast(result.cause))
            is Result.NoNetwork<User> -> _events.emit(UiEvent.ShowToast("No internet"))
            is Result.Success<User> -> {
                setName(result.data.name)
                setUserId(result.data.userId)
            }
        }
    }

    private suspend fun handleUserSaveResult(result: Result<Boolean>) {
        when (result) {
            is Result.Failure<Boolean> -> _events.emit(UiEvent.ShowToast(result.cause))
            is Result.NoNetwork<Boolean> -> _events.emit(UiEvent.ShowToast("No internet"))
            is Result.Success<Boolean> -> _events.emit(UiEvent.ShowToast("Success saving"))
        }
    }

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel() as T
        }
    }
}