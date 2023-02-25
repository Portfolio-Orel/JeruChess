package com.orels.jeruchess.android.presentation.main.components

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color

data class CardColor(
    val background: Color,
    val primary: Color,
)

class CardColors {

    companion object {

        private val colorsUsed = mutableStateListOf<CardColor>()

        fun getCardColor(darkTheme: Boolean): CardColor {
            var color = if (darkTheme) {
                (darkColors.filter { !(colorsUsed.contains(it)) }).lastOrNull()
            } else {
                (lightColors.filter { !(colorsUsed.contains(it)) }).lastOrNull()
            }
            if (color != null) {
                colorsUsed.add(color)
            } else {
                colorsUsed.clear()
                color = if (darkTheme) {
                    darkColors.last()
                } else {
                    lightColors.last()
                }
            }
            return color
        }


        private val lightColors = listOf(
            CardColor(
                background = Color(0xFFE0E0E0),
                primary = Color(0xFF473C61),
            ),
            CardColor(
                background = Color(0xFFCB9CA3),
                primary = Color(0xFF5B2529),
            ),
            CardColor(
                background = Color(0xFF9E9E9E),
                primary = Color(0xFF000000),
            ),
            CardColor(
                background = Color(0xFF9DCBC9),
                primary = Color(0xFF136A65),
            ),
            CardColor(
                background = Color(0xFFC1CB9D),
                primary = Color(0xFF42510B),
            ),
            CardColor(
                background = Color(0xFFE4B875),
                primary = Color(0xFF66461C),
            ),
            CardColor(
                background = Color(0xFFB0BBBC),
                primary = Color(0xFF3A4646),
            ),
        )

        private val darkColors = listOf(
            CardColor(
                background = Color(0xFFE0E0E0),
                primary = Color(0xFF473C61),
            ),
            CardColor(
                background = Color(0xFFCB9CA3),
                primary = Color(0xFF5B2529),
            ),
            CardColor(
                background = Color(0xFF9E9E9E),
                primary = Color(0xFF000000),
            ),
            CardColor(
                background = Color(0xFF9DCBC9),
                primary = Color(0xFF136A65),
            ),
            CardColor(
                background = Color(0xFFC1CB9D),
                primary = Color(0xFF42510B),
            ),
            CardColor(
                background = Color(0xFFE4B875),
                primary = Color(0xFF66461C),
            ),
            CardColor(
                background = Color(0xFFB0BBBC),
                primary = Color(0xFF3A4646),
            ),
        )
    }
}