package com.orels.jeruchess.main.data.events_participants

import com.orels.jeruchess.main.domain.model.EventParticipant
import com.orels.jeruchess.main.domain.model.PaymentType
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class EventParticipantDto(
    @SerialName("id") val id: String? = null,
    @SerialName("user_id") val userId: String,
    @SerialName("event_id") val eventId: String,
    @SerialName("is_paid") val isPaid: Boolean? = false,
    @SerialName("amount_paid") val paidAmount: Long? = 0L,
    @SerialName("paid_at") val paidAt: Long? = null,
    @SerialName("payment_type") val paymentType: String? = null,
    @SerialName("is_active") val isActive: Boolean? = true
) {
    fun toEventParticipant() = EventParticipant(
        id = id,
        userId = userId,
        eventId = eventId,
        isPaid = isPaid,
        paidAmount = paidAmount,
        paidAt = paidAt,
        paymentType = paymentType?.let { PaymentType.valueOf(it) },
        isActive = isActive ?: true
    )
}

@kotlinx.serialization.Serializable
data class AddEventParticipantsResponse(
    @SerialName("ids") val eventParticipantIds: List<String>
)

fun EventParticipant.toEventParticipantDto() = EventParticipantDto(
    id = id,
    userId = userId,
    eventId = eventId,
    isPaid = isPaid,
    paidAmount = paidAmount,
    paidAt = paidAt,
    paymentType = paymentType?.name,
    isActive = isActive
)

fun List<EventParticipant>.toEventParticipantsDto() = map { it.toEventParticipantDto() }