package com.neurasamu.build.neura_dub.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary = NeonPurpleLight,
    secondary = NeonTeal,
    tertiary = NeonPink,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onPrimary = DarkBackground,
    onSecondary = DarkBackground,
    onBackground = LightBackground,
    onSurface = LightBackground
)

private val LightColors = lightColorScheme(
    primary = NeonPurple,
    secondary = NeonTeal,
    tertiary = NeonPink,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = LightSurface,
    onBackground = DarkBackground,
    onSurface = DarkBackground
)

@Composable
fun NeuraDubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
