package com.orels.jeruchess.android.presentation.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.People
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.orels.jeruchess.android.R
import com.orels.jeruchess.android.domain.exceptions.dayOfMonth
import com.orels.jeruchess.android.domain.exceptions.toDayName
import com.orels.jeruchess.android.domain.exceptions.toMonthAcronym
import com.orels.jeruchess.android.presentation.components.ActionButton
import com.orels.jeruchess.main.domain.model.Event
import com.orels.jeruchess.main.domain.model.EventParticipant
import com.orels.jeruchess.main.domain.model.Game

typealias OnRegister = (event: Event) -> Unit
typealias OnCancelRegistration = (event: Event) -> Unit

@Composable
fun EventCard(
    event: Event,
    onRegister: OnRegister,
    onCancelRegistration: OnCancelRegistration,
    modifier: Modifier = Modifier,
    game: Game? = null,
    maxRounds: Int = 1,
    participants: List<EventParticipant> = emptyList(),
    isRegistered: Boolean = false,
    isLoading: Boolean = false,
) {
    val isDarkTheme = isSystemInDarkTheme()
    val cardColor = remember { CardColors.getCardColor(isDarkTheme) }
    val shape = RoundedCornerShape(24.dp)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        shape = shape,
        elevation = 8.dp,
        backgroundColor = cardColor.background,
        contentColor = cardColor.primary,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            EventDate(
                event = event,
            )
            Spacer(modifier = Modifier.weight(1f))
            EventDetails(
                event = event,
                game = game,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ParticipantsNumber(participants = participants.count())
                RoundNumber(maxRounds = maxRounds, currentRound = event.roundNumber)
                Spacer(modifier = Modifier.weight(1f))
                Price(event = event, isPaid = isRegistered)
                ActionButton(
                    modifier = Modifier.padding(top = 8.dp),
                    onClick = {
                        if (isRegistered) onCancelRegistration(event) else onRegister(
                            event
                        )
                    },
                    text = if (isRegistered) stringResource(R.string.cancel) else stringResource(
                        R.string.register
                    ),
                    fontWeight = FontWeight.Normal,
                    fontStyle = MaterialTheme.typography.body1,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (isRegistered) MaterialTheme.colors.error else MaterialTheme.colors.secondary,
                        contentColor = if (isRegistered) MaterialTheme.colors.onError else MaterialTheme.colors.onSecondary
                    ),
                    elevation = ButtonDefaults.elevation(0.dp),
                    isLoading = isLoading,
                )
            }
        }
    }
}

@Composable
private fun EventDate(
    event: Event,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically)
    ) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 0.dp),
            text = stringResource(id = event.date.toDayName()),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
        )
        Text(
            text = event.date.dayOfMonth.toString() + "\n" + stringResource(id = event.date.toMonthAcronym()),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.h3,
        )
    }
}

@Composable
private fun EventDetails(
    event: Event,
    modifier: Modifier = Modifier,
    game: Game? = null,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(0.6f)
            .background(Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = event.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Normal),
        )
        Spacer(Modifier.weight(1f))
        if (game != null) {
            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(id = R.drawable.round_timer_24),
                    contentDescription = stringResource(R.string.timer)
                )
                Text(
                    text = "${game.timeStartMin}+${game.incrementBeforeTimeControlSec}",
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                )
            }
        }
    }
}

@Composable
private fun Price(
    event: Event,
    modifier: Modifier = Modifier,
    isPaid: Boolean = false,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterHorizontally)
    ) {
        if (isPaid) {
            Image(
                painter = painterResource(id = R.drawable.paid),
                contentDescription = stringResource(R.string.paid),
                modifier = Modifier
                    .size(36.dp)
                    .padding(horizontal = 2.dp)
            )
        } else {
            Text(
                text = event.price.toString(),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.h5,
            )
            Currency(currency = event.currency, modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
private fun ParticipantsNumber(
    participants: Int,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Rounded.People,
            contentDescription = stringResource(R.string.participants),
            modifier = Modifier
                .size(18.dp)
                .padding(horizontal = 2.dp)
        )
        Text(
            text = "$participants",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
        )
    }
}

@Composable
private fun RoundNumber(
    maxRounds: Int,
    currentRound: Int,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .size(18.dp)
                .padding(horizontal = 2.dp),
            painter = painterResource(id = R.drawable.chess),
            contentDescription = stringResource(R.string.round)
        )
        Text(
            text = "$currentRound/$maxRounds",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
        )
    }
}