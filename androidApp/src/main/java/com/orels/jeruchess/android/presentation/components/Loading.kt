package com.orels.jeruchess.android.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Loading(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onBackground,
    size: Dp? = null,
    strokeWidth: Dp = 2.dp
) {
    CircularProgressIndicator(
        modifier = if (size != null) modifier.size(size) else modifier,
        color = color,
        strokeWidth = strokeWidth
    )
}