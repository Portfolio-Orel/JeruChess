package com.orels.jeruchess.main.domain.model

enum class PaymentType {
    Cash,
    Card
}

data class EventParticipant(
    var eventId: String,
    var userId: String,
    var isPaid: Boolean,
    var paidAt: Long?,
    var paidAmount: Long?,
    var paymentType: PaymentType?,
    var isActive: Boolean
)