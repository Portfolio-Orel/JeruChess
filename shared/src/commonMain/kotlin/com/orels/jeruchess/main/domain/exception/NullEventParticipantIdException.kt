package com.orels.jeruchess.main.domain.exception

class NullEventParticipantIdException(userId: String, eventId: String) :
    Exception("EventParticipant with userId: $userId and eventId: $eventId has null id")