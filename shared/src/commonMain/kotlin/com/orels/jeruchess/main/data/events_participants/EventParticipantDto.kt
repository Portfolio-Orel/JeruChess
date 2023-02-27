package com.orels.jeruchess.main.data.events_participants

import com.orels.jeruchess.main.domain.model.EventParticipant
import com.orels.jeruchess.main.domain.model.PaymentType
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class EventParticipantDto(
    @SerialName("user_id") val userId: String,
    @SerialName("event_id") val eventId: String,
    @SerialName("is_paid") val isPaid: Boolean,
    @SerialName("amount_paid") val paidAmount: Long,
    @SerialName("paid_at") val paidAt: Long,
    @SerialName("payment_type") val paymentType: String,
    @SerialName("is_active") val isActive: Boolean?
) {
    fun toEventParticipant() = EventParticipant(
        userId = userId,
        eventId = eventId,
        isPaid = isPaid,
        paidAmount = paidAmount,
        paidAt = paidAt,
        paymentType = PaymentType.valueOf(paymentType),
        isActive = isActive ?: true
    )
}

fun List<EventParticipantDto>.toEventParticipants() = map { it.toEventParticipant() }

fun EventParticipant.toEventParticipantDto() = EventParticipantDto(
    userId = userId,
    eventId = eventId,
    isPaid = isPaid,
    paidAmount = paidAmount ?: 0,
    paidAt = paidAt ?: 0,
    paymentType = paymentType?.name ?: PaymentType.Cash.name,
    isActive = isActive
)

fun List<EventParticipant>.toEventParticipantsDto() = map { it.toEventParticipantDto() }