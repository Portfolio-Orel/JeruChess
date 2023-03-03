package com.orels.jeruchess

enum class Environment(val value: String) {
    DEV("dev"),
    PROD("prod")
}

object NetworkConstants {
    private val environment = Environment.DEV
    val BASE_URL =
        if (environment == Environment.DEV) "https://g33llytwga.execute-api.us-east-1.amazonaws.com" else "https://g33llytwga.execute-api.us-east-1.amazonaws.com"
}