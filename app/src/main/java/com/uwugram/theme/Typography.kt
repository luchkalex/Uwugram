package com.uwugram.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.uwugram.R

private val RobotoFontFamily = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_light, FontWeight.Light),
)

val AppTypographyDark = Typography(
    h1 = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium
    ),
    h2 = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
    ),
    subtitle1 = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    ),
    body1 = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    ),
    body2 = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = FadedTextColorDark
    ),
    overline = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 10.sp,
        fontWeight = FontWeight.Normal,
        color = FadedTextColorDark
    ),
    caption = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    )
)

val AppTypography = Typography(
    h1 = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium
    ),
    h2 = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
    ),
    subtitle1 = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    ),
    body1 = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    ),
    body2 = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = FadedTextColorDark
    ),
    overline = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 10.sp,
        fontWeight = FontWeight.Normal,
        color = FadedTextColorDark
    ),
    caption = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    )
)