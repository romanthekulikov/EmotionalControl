package ru.kulikov.auth.domain

internal interface AuthorizeContract {
    fun auth()
    fun createAccount()
}