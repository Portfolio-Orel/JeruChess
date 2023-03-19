package com.orels.jeruchess.main.data.main

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class DtoWrapper<T>(
    @SerialName("statusCode") val statusCode: Int? = 404,
    @SerialName("body") val body: T? = null,
    @SerialName("error") val error: String? = null
)