package com.nytimesmostviewedarticles.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val LightTypography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = Black
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = Black
    ),
    subtitle1 = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Italic,
        fontSize = 14.sp,
        color = DarkGray
    ),
    subtitle2 = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Italic,
        fontSize = 12.sp,
        color = DarkGray
    ),
    h1 = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        textAlign = TextAlign.Center,
        color = Black
    ),
    h2 = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        textAlign = TextAlign.Center,
        color = Black
    ),
    h3 = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = Black
    ),
    h4 = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 18.sp,
        color = Black
    ),
    h6 = TextStyle(             // errors
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
        color = Black
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 12.sp,
        color = Black
    )
)

val DarkTypography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = White
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = White
    ),
    subtitle1 = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Italic,
        fontSize = 14.sp,
        color = LightGray
    ),
    subtitle2 = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Italic,
        fontSize = 12.sp,
        color = LightGray
    ),
    h1 = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        textAlign = TextAlign.Center,
        color = White
    ),
    h2 = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        textAlign = TextAlign.Center,
        color = White
    ),
    h3 = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = White
    ),
    h4 = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 18.sp,
        color = White
    ),
    h6 = TextStyle(             // errors
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
        color = White
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 12.sp,
        color = White
    )
)