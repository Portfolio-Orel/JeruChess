package com.orels.jeruchess.android.core.theme

// convert all colors from shared Colors object to Color
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import com.orels.jeruchess.presentation.Colors

val lightColors = lightColors(
    primary = Color(Colors.md_theme_light_primary),
    primaryVariant = Color(Colors.md_theme_light_outlineVariant),
    secondary = Color(Colors.md_theme_light_secondary),
    secondaryVariant = Color(Colors.md_theme_light_surfaceVariant),
    background = Color(Colors.md_theme_light_background),
    surface = Color(Colors.md_theme_light_surface),
    error = Color(Colors.md_theme_light_error),
    onPrimary = Color(Colors.md_theme_light_onPrimary),
    onSecondary = Color(Colors.md_theme_light_onSecondary),
    onBackground = Color(Colors.md_theme_light_onBackground),
    onSurface = Color(Colors.md_theme_light_onSurface),
    onError = Color(Colors.md_theme_light_onError),
)

val darkColors = darkColors(
    primary = Color(Colors.md_theme_dark_primary),
    primaryVariant = Color(Colors.md_theme_dark_outlineVariant),
    secondary = Color(Colors.md_theme_dark_secondary),
    secondaryVariant = Color(Colors.md_theme_dark_surfaceVariant),
    background = Color(Colors.md_theme_dark_background),
    surface = Color(Colors.md_theme_dark_surface),
    error = Color(Colors.md_theme_dark_error),
    onPrimary = Color(Colors.md_theme_dark_onPrimary),
    onSecondary = Color(Colors.md_theme_dark_onSecondary),
    onBackground = Color(Colors.md_theme_dark_onBackground),
    onSurface = Color(Colors.md_theme_dark_onSurface),
    onError = Color(Colors.md_theme_dark_onError),
)