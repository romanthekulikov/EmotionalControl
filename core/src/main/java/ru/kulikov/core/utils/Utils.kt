package ru.kulikov.core.utils

import android.util.Patterns

fun isEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}