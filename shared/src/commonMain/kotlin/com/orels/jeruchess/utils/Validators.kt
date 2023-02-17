package com.orels.jeruchess.utils

class Validators {
    companion object {
        private val emailPattern = Regex("[a-zA-Z\\d._%+-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}")

        fun isEmailValid(email: String): Boolean = emailPattern.matches(email)

        fun isPhoneNumberValid(number: String): Boolean = number.length == 10

    }
}