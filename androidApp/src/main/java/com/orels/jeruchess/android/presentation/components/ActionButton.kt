package com.orels.jeruchess.android.presentation.components
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isPrimary: Boolean = true,
    text: String,
    isLoading: Boolean = false,
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isPrimary) MaterialTheme.colors.primary else Color.Transparent,
        ),
        onClick = onClick,
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .zIndex(2f),
                contentAlignment = Alignment.Center
            )
            {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp),
                    color = if (isPrimary) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onBackground,
                    strokeWidth = 2.dp
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth()
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.h4,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color =
                if (isLoading) Color.Transparent
                else if (isPrimary) MaterialTheme.colors.onPrimary
                else MaterialTheme.colors.onBackground
            )
        }
    }
}