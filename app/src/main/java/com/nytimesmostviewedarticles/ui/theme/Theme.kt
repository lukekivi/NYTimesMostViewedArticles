package com.nytimesmostviewedarticles.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = White,
    primaryVariant = LightGray,
    secondary = DarkGray,
    secondaryVariant = Cyan,
    background = AlmostBlack
)

private val LightColorPalette = lightColors(
    primary = Black,
    primaryVariant = DarkGray,
    secondary = LightGray,
    secondaryVariant = Cobalt,
    background = White
)

@get:Composable
val Colors.Hyperlink: Color
    get() = if (isLight) Cobalt else Cyan

@Composable
fun NYTimesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val typography = if (darkTheme) {
        DarkTypography
    } else {
        LightTypography
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = Shapes,
        content = content
    )
}