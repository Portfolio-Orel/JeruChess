package com.orels.jeruchess

enum class Environment(val value: String) {
    DEV("dev"),
    PROD("prod")
}

object NetworkConstants {
    private val environment = Environment.DEV
    val BASE_URL =
        if (environment == Environment.DEV) "chessdev.cn9qgwm3araj.us-east-2.rds.amazonaws.com" else "chessdev.cn9qgwm3araj.us-east-2.rds.amazonaws.com"
}