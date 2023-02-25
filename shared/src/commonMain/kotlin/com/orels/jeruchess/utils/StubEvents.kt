package com.orels.jeruchess.utils

import com.orels.jeruchess.main.domain.model.Event

class StubEvents {

    companion object {
        private fun getRandomDateFromLastYear(): Long {
            val now = 1677313326L
            val lastYear = now - 31556926L
            return (lastYear..now).random()
        }

        val Events = (0..9).map {
            Event(
                id = it.toString(),
                name = "Event $it",
                description = "Description $it",
                date = getRandomDateFromLastYear(),
                price = 60L + it,
                currency = "Currency $it",
                roundNumber = 5 + it,
                eventType = when (it % 2) {
                    0 -> "Tournament"
                    1 -> "Single"
                    else -> "Single"
                },
                eventFormat = when (it % 5) {
                    0 -> "Swiss"
                    1 -> "Scheveningen"
                    2 -> "GroupDuel"
                    3 -> "Weinstein"
                    else -> "Levin"
                },
                isRatingIsrael = it % 2 == 0,
                isRatingFide = it % 2 == 0,
                ratingType = when (it % 3) {
                    0 -> "Standard"
                    1 -> "Rapid"
                    else -> "Blitz"
                }
            )
        }
    }
}