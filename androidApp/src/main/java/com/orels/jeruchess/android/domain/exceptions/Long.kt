@file:Suppress("MoveVariableDeclarationIntoWhen")

package com.orels.jeruchess.android.domain.exceptions

import androidx.annotation.StringRes
import com.orels.jeruchess.android.R
import java.util.*

@StringRes
fun Long.toMonthAcronym(): Int {
    return when (month) {
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
    return when (dayOfWeek) {
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

val Long.millisecondsLong
    get() =
        if (this.toString().length > 10) {
            this
        } else {
            this * 1000
        }

val Long.dayOfMonth get() = calendar().get(Calendar.DAY_OF_MONTH) + 1
val Long.dayOfWeek get() = calendar().get(Calendar.DAY_OF_WEEK)
val Long.month get() = calendar().get(Calendar.MONTH)

fun Long.calendar(): Calendar {
    val date = Date(this.millisecondsLong)
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar
}