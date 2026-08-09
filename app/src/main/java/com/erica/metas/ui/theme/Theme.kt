package com.erica.metas.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.erica.metas.data.ThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = GreenDarkPrimary,
    onPrimary = BackgroundDark,
    primaryContainer = GreenDarkBg,
    background = BackgroundDark,
    surface = SurfaceDark,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    outline = CardBorderDark
)

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = SurfaceLight,
    primaryContainer = GreenLightBg,
    background = BackgroundLight,
    surface = SurfaceLight,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    outline = CardBorderLight
)

@Composable
fun AppEricaTheme(
    themeMode: ThemeMode = ThemeMode.LIGHT,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode) {
        ThemeMode.LIGHT -> LightColorScheme
        ThemeMode.DARK -> DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
