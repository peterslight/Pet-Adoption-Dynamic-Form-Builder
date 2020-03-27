package com.peterstev.petadoption.utils

import android.util.Log
import java.util.*


fun LOGGER(message: String) {
    Log.d("TAG", message)
}

fun validateEmail(email: String): Boolean {
    return email.isEmailValid()
}

fun String.isEmailValid() =
    PATTERN.matcher(this).matches()

fun isCapital(text: String): Boolean {
    if (text.isNotEmpty()) {
        return capitalize(text).get(0).isUpperCase()
    }
    return false
}

fun capitalize(s: String?): String {
    if (s == null || s.isEmpty()) {
        return ""
    }
    val first = s[0]
    return if (Character.isUpperCase(first)) {
        first.toString() + s.substring(1).toLowerCase(Locale.getDefault())
    } else {
        Character.toUpperCase(first).toString() + s.substring(1).toLowerCase(Locale.getDefault())
    }
}