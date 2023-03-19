package com.orels.jeruchess.main.domain.model

enum class PaymentType {
    Cash,
    Card
}

data class EventParticipant(
    var eventId: String,
    var userId: String,
    var isPaid: Boolean? = false,
    var paidAt: Long? = null,
    var paidAmount: Long? = null,
    var paymentType: PaymentType? = null,
    var isActive: Boolean = true,
)