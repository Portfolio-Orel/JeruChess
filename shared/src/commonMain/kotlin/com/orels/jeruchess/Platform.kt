package com.orels.jeruchess

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform