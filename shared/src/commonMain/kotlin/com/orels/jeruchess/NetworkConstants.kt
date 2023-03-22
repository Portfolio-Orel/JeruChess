package com.orels.jeruchess

enum class Environment(val value: String) {
    LOCAL("local"),
    DEV("dev"),
    PROD("prod")
}

object NetworkConstants {
    private val environment = Environment.DEV
    val BASE_URL = when (environment) {
        Environment.LOCAL -> "http://10.0.2.2:4000"
        Environment.DEV -> "https://g33llytwga.execute-api.us-east-1.amazonaws.com"
        Environment.PROD -> "https://g33llytwga.execute-api.us-east-1.amazonaws.com"
    }

}