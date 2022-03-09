package com.uwugram.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFF858585)
val PrimaryVariant = Color(0xFF5A5A5A)
val Secondary = Color(0xFF1F1F1F)
val SecondaryVariant = Color(0xFF00EAFF)
val Background = Color(0xFF101010)
val Surface = Color(0xFF171717)
val Error = Color(0xFF558B2F)
val OnPrimary = Color(0xFFFFFFFF)
val OnSecondary = Color(0xFFFFFFFF)
val OnBackground = Color(0xFFFFFFFF)
val OnSurface = Color(0xFFFFFFFF)
val OnError = Color(0xFFFF0000)

val PrimaryDark = Color(0xFF858585)
val PrimaryVariantDark = Color(0xFF5A5A5A)//faded
val SecondaryDark = Color(0xFF1F1F1F)
val SecondaryVariantDark = Color(0xFF00EAFF)
val BackgroundDark = Color(0xFF101010)
val SurfaceDark = Color(0xFF171717)
val ErrorDark = Color(0xFF558B2F)
val OnPrimaryDark = Color(0xFFFFFFFF)
val OnSecondaryDark = Color(0xFFFFFFFF)
val OnBackgroundDark = Color(0xFFFFFFFF)
val OnSurfaceDark = Color(0xFFFFFFFF)
val OnErrorDark = Color(0xFFFF0000)

val Dim = BackgroundDark

val OwnMessageBackground = Color(0xFF2B2B2B)
val OthersMessageBackground = Color(0xFF454545)

val FadedTextColorDark = Color(0xFF848484)

val LightTheme = lightColors(
    primary = Primary,
    primaryVariant = PrimaryVariant,
    secondary = Secondary,
    secondaryVariant = SecondaryVariant,
    background = Background,
    surface = Surface,
    error = Error,
    onPrimary = OnPrimary,
    onBackground = OnBackground,
    onError = OnError,
    onSecondary = OnSecondary,
    onSurface = OnSurface
)
val DarkTheme = darkColors(
    primary = PrimaryDark,
    primaryVariant = PrimaryVariantDark,
    secondary = SecondaryDark,
    secondaryVariant = SecondaryVariantDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    error = ErrorDark,
    onPrimary = OnPrimaryDark,
    onBackground = OnBackgroundDark,
    onError = OnErrorDark,
    onSecondary = OnSecondaryDark,
    onSurface = OnSurfaceDark
)

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = when {
            isDarkTheme -> DarkTheme
            else -> LightTheme
        },
        typography = when {
            isDarkTheme -> AppTypographyDark
            else -> AppTypography
        },
        shapes = AppShapes,
        content = {
            Scaffold {
                content()
            }
        }
    )
}