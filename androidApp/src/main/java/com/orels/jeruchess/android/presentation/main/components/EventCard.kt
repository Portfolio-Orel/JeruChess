package com.orels.jeruchess.android.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.orels.jeruchess.android.domain.exceptions.getDayOfMonth
import com.orels.jeruchess.android.domain.exceptions.toDayName
import com.orels.jeruchess.android.domain.exceptions.toMonthAcronym
import com.orels.jeruchess.main.domain.model.Event

@Composable
fun EventCard(
    event: Event,
    modifier: Modifier = Modifier,
) {
    val isDarkTheme = isSystemInDarkTheme()
    val cardColor = remember { CardColors.getCardColor(isDarkTheme) }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(color = cardColor.backgroundColor)
            .padding(4.dp),
        backgroundColor = cardColor.backgroundColor,
        contentColor = cardColor.primary,
    ) {
        Row(modifier = Modifier) {
            EventDate(
                modifier = Modifier.background(color = cardColor.backgroundColor),
                event = event,
                cardColors = cardColor,
            )
        }
    }
}

@Composable
fun EventDate(
    event: Event,
    cardColors: CardColor,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxHeight()) {
        Text(
            modifier = Modifier.padding(4.dp),
            text = stringResource(id = event.date.toDayName()),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6,
            color = cardColors.primary
        )
        Text(
            modifier = Modifier.padding(2.dp),
            text = event.date.getDayOfMonth().toString(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h3,
            color = cardColors.primary
        )
        Text(
            modifier = Modifier.padding(2.dp),
            text = stringResource(id = event.date.toMonthAcronym()),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h3,
            color = cardColors.primary
        )
    }
}