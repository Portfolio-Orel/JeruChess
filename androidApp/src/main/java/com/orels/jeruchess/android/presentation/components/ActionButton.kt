package com.orels.jeruchess.android.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = MaterialTheme.colors.onBackground,
        contentColor = MaterialTheme.colors.background,
    ),
    text: String,
    isLoading: Boolean = false,
) {
    Button(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = colors,
        onClick = onClick,
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(2f),
                contentAlignment = Alignment.Center
            )
            {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp),
                    color = colors.contentColor(enabled = true).value,
                    strokeWidth = 2.dp
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                color =
                if (isLoading) Color.Transparent
                else colors.contentColor(enabled = true).value
            )
        }
    }
}