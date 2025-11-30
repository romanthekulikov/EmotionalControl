package ru.kulikov.core.utils

import android.util.Patterns

fun isEmail(string: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(string).matches()
}