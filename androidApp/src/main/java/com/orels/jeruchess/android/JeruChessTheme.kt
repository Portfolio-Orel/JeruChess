package com.orels.jeruchess.android

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun JeruChessTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val font = FontFamily(
        Font(
            resId = R.font.roboto_regular,
            weight = FontWeight.Normal
        ),
        Font(
            resId = R.font.roboto_black,
            weight = FontWeight.Medium
        ),
        Font(
            resId = R.font.roboto_bold,
            weight = FontWeight.Bold
        ),
        Font(
            resId = R.font.roboto_light,
            weight = FontWeight.Light
        ),
        Font(
            resId = R.font.roboto_medium,
            weight = FontWeight.Medium
        ),
        Font(
            resId = R.font.roboto_thin,
            weight = FontWeight.Thin
        ),
        Font(
            resId = R.font.roboto_italic,
            style = FontStyle.Italic,
        ),
        Font(
            resId = R.font.roboto_bold_italic,
            weight = FontWeight.Bold,
            style = FontStyle.Italic
        ),
        Font(
            resId = R.font.roboto_light_italic,
            weight = FontWeight.Light,
            style = FontStyle.Italic
        ),
        Font(
            resId = R.font.roboto_medium_italic,
            weight = FontWeight.Medium,
            style = FontStyle.Italic
        ),
        Font(
            resId = R.font.roboto_thin_italic,
            weight = FontWeight.Thin,
            style = FontStyle.Italic
        )
    )

    val colors = if (darkTheme) {
        com.orels.jeruchess.android.core.theme.darkColors
    } else {
        com.orels.jeruchess.android.core.theme.lightColors
    }
    val typography = Typography(
        defaultFontFamily = font,
        body1 = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    )
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
