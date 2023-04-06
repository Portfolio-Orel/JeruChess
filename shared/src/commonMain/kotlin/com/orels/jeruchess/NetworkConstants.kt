package com.orels.jeruchess

enum class Environment(val value: String) {
    LOCAL("local"),
    DEV("dev"),
    PROD("prod")
}

object NetworkConstants {
    private val environment = Environment.LOCAL
    val BASE_URL = when (environment) {
        Environment.LOCAL -> "http://10.0.2.2:4000"
        Environment.DEV -> "https://0j3kvj5lpl.execute-api.us-east-1.amazonaws.com"
        Environment.PROD -> "https://0j3kvj5lpl.execute-api.us-east-1.amazonaws.com"
    }

}