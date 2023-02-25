package com.orels.jeruchess.android.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.orels.jeruchess.android.domain.exceptions.dayOfMonth
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
    val shape = RoundedCornerShape(24.dp)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(4.dp),
        shape = shape,
        elevation = 4.dp,
        backgroundColor = cardColor.background,
        contentColor = cardColor.primary,
    ) {
        Row(modifier = Modifier) {
            EventDate(
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
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(14.dp)
            .background(Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically)
    ) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = stringResource(id = event.date.toDayName()),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            color = cardColors.primary
        )
        Text(
            text = event.date.dayOfMonth.toString() + "\n" + stringResource(id = event.date.toMonthAcronym()),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.h3,
            color = cardColors.primary
        )
    }
}