package com.orels.jeruchess.android.core.presentation

import androidx.annotation.StringRes
import com.orels.jeruchess.android.R
data class NavigationArgument(val key: String, val value: String, val defaultValue: String)

enum class Screens(val route: String, @StringRes val label: Int = R.string.empty_string) {
    Login("login"),
    Register("register"),
    Main("main", R.string.label_main),
    ForgotPassword("forgotPassword");

    companion object {
        const val navigationArgumentDefaultValue = "-1"
    }


    fun withArgs(args: Map<String, String>?): String {
        val newRoute = buildString {
            append(route)
            args?.forEach { (key, value) ->
                if (key.isNotEmpty() && value.isNotEmpty()) {
                    append("?$key=$value")
                } else {
                    append("?$key=$navigationArgumentDefaultValue")
                }
            }
        }
        return newRoute
    }

    fun withArgsForRoute(vararg args: String?): String {
        val newRoute = buildString {
            append(route)
            args.forEach { value ->
                if (value != null && value.isNotEmpty()) {
                    append("?${value}={$value}")
                }
            }
        }
        return newRoute
    }
}



object RoutesArguments {
    const val PRE_INSERTED_PHONE_NUMBER = "preInsertedPhoneNumber"
    const val PRE_INSERTED_EMAIL = "preInsertedEmail"
}