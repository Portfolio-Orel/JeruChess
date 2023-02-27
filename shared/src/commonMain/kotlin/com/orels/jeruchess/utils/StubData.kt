package com.orels.jeruchess.utils

import com.orels.jeruchess.main.domain.model.*

class StubData {

    companion object {
        private fun getRandomDateFromLastYear(): Long {
            val now = 1677313326L
            val lastYear = now - 31556926L
            return (lastYear..now).random()
        }

        val Events: List<Event>
            get() {
                val list: List<Event> = (0..9).map {
                    Event(
                        id = (it % 3).toString(),
                        name = when (it % 3) {
                            0 -> "Classic Chess Tournament"
                            1 -> "Rapid Chess Tournament"
                            else -> "Blitz Chess Tournament"
                        },
                        description = when (it % 3) {
                            0 -> "50 + 10 chess game."
                            1 -> "15 + 4 chess game."
                            else -> "4 + 3 chess game."
                        },
                        date = getRandomDateFromLastYear(),
                        price = 60L + it % 3,
                        currency = Currency.ILS,
                        roundNumber = 1,
                        eventType = "Tournament",
                        eventFormat = when (it % 3) {
                            0 -> "Swiss"
                            1 -> "Scheveningen"
                            2 -> "GroupDuel"
                            else -> "Swiss"
                        },
                        isRatingIsrael = it % 3 == 0,
                        isRatingFide = it % 3 == 0,
                        ratingType = when (it % 3) {
                            0 -> "Standard"
                            1 -> "Rapid"
                            else -> "Blitz"
                        },
                        gameId = (it % 4).toString()
                    )
                }.sortedBy { it.date }

                val map = list.groupBy { it.id }
                map.forEach {
                    it.value.forEachIndexed { index, _ ->
                        val roundNumber = it.value.size - (it.value.size - index) + 1
                        list.first { event -> event.date == it.value[index].date }.apply {
                            this.roundNumber = roundNumber
                        }
                    }
                }
                return list
            }

        val EventsParticipants = (0..40).map {
            EventParticipant(
                userId = it.toString(),
                eventId = (it % 3).toString(),
                isPaid = it % 2 == 0,
                paidAt = getRandomDateFromLastYear(),
                paidAmount = 60L + it % 3,
                paymentType = if(it % 2 == 0) PaymentType.Cash else PaymentType.Card,
                isActive = true
            )
        }

        val Games = (0..3).map {
            Game(
                id = it.toString(),
                timeStart = when (it) {
                    0 -> 3L
                    1 -> 15L
                    2 -> 50L
                    else -> 60L
                },
                incrementBeforeTimeControl = when (it) {
                    0 -> 2L
                    1 -> 5L
                    2 -> 10L
                    else -> 30L
                },
                movesNumToTimeControl = 0,
                timeBumpAfterTimeControl = 0,
                incrementAfterTimeControl = 0,
                type = when (it) {
                    0 -> GameType.BLITZ
                    1 -> GameType.RAPID
                    2 -> GameType.CLASSIC
                    else -> GameType.CLASSIC
                }
            )
        }

    }
}