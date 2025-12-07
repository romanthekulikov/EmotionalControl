package ru.kulikov.core.utils.base

sealed class UiEvent() {
    data class ShowToast(val message: String) : UiEvent()
}