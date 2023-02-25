@file:Suppress("MoveVariableDeclarationIntoWhen")

package com.orels.jeruchess.android.domain.exceptions

import androidx.annotation.StringRes
import com.google.type.DateTime
import com.orels.jeruchess.android.R
import java.util.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

fun Long.toDayOfMonth(): Int =
    DateTime.newBuilder().setSeconds(this.secondsInt).build().day

@StringRes
fun Long.toMonthAcronym(): Int {
    return when (getMonth()) {
        1 -> R.string.january_acronym
        2 -> R.string.february_acronym
        3 -> R.string.march_acronym
        4 -> R.string.april_acronym
        5 -> R.string.may_acronym
        6 -> R.string.june_acronym
        7 -> R.string.july_acronym
        8 -> R.string.august_acronym
        9 -> R.string.september_acronym
        10 -> R.string.october_acronym
        11 -> R.string.november_acronym
        12 -> R.string.december_acronym
        else -> R.string.january_acronym
    }
}

@StringRes
fun Long.toDayName(): Int {
    return when (getDayOfWeek()) {
        1 -> R.string.sunday
        2 -> R.string.monday
        3 -> R.string.tuesday
        4 -> R.string.wednesday
        5 -> R.string.thursday
        6 -> R.string.friday
        7 -> R.string.saturday
        else -> R.string.sunday
    }
}

val Long.secondsInt get() = this.seconds.toInt(DurationUnit.SECONDS)

fun Long.getDayOfMonth(): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return calendar.get(Calendar.DAY_OF_MONTH)
}

fun Long.getDayOfWeek(): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return calendar.get(Calendar.DAY_OF_WEEK)
}

fun Long.getMonth(): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return calendar.get(Calendar.MONTH)
}