package com.orels.jeruchess.android.core.presentation

object Routes {
    const val LOGIN = "login"
    const val FORGOT_PASSWORD = "forgot_password"
    const val REGISTER = "register"
    const val MAIN = "main"

    fun withArgs(route: String, vararg args: String?): String {
        return buildString {
            append(route)
            args.forEach { value ->
                if (value != null && value.isNotEmpty()) {
                    append("/$value")
                }
            }
        }
    }
}

object RoutesArguments {
    const val PRE_INSERTED_PHONE_NUMBER = "preInsertedPhoneNumber"
    const val PRE_INSERTED_EMAIL = "preInsertedEmail"
}