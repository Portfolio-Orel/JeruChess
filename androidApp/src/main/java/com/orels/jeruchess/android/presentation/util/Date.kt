package com.orels.jeruchess.android.presentation.util

import java.text.SimpleDateFormat
import java.util.*

val Date.registerString: String
    get() = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(this)

fun Long.toRegisterDate(): String =
    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(this))

fun String.toRegisterDate(): Date? =
    try {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(this)
    } catch (e: Exception) {
        null
    }

fun String.toRegisterDateLong(): Long =
    try {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(this)?.time ?: throw Exception()
    } catch (e: Exception) {
        // TODO: Log that
        0L
    }

fun String?.isDateValid(): Boolean = this != null && try {
    if (this.length != 10) {
        false
    } else {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(this)
        true
    }
} catch (e: Exception) {
    false
}