package ru.kulikov.auth.domain

interface AuthorizeContract {
    fun auth(email: String, pass: String)
    fun createAccount(email: String, pass: String, confirmPass: String)
}