package com.dreamrecall.ui.theme

import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PurpleBlueAccent,
    secondary = DarkPurpleAccent,
    tertiary = PurpleBlueAccent,
    background = BlackBackground,
    surface = CardBackground,
    onPrimary = SoftWhiteText,
    onSecondary = SoftWhiteText,
    onTertiary = SoftWhiteText,
    onBackground = SoftWhiteText,
    onSurface = SoftWhiteText,
)

private object SmoothRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = PurpleBlueAccent

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
        Color(0xFF6A00FF),
        lightTheme = !isSystemInDarkTheme()
    ).copy(pressedAlpha = 0.1f, hoveredAlpha = 0.04f)
}

@Composable
fun DreamRecallTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography
    ) {
        CompositionLocalProvider(LocalRippleTheme provides SmoothRippleTheme) {
            content()
        }
    }
}
