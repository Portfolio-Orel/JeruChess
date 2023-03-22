package com.orels.jeruchess.main.data.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PaymentDto {
    @SerialName("payment_url") val paymentUrl: String? = null
    @SerialName("error_code") val errorCode: Int? = null
}