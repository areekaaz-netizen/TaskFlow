package com.example.taskflow.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFA996FF),
    onPrimary = Color.White,

    primaryContainer = Color(0xFF382870),
    onPrimaryContainer = Color(0xFFF0EBFF),

    secondary = Color(0xFFB8A9EA),
    onSecondary = Color(0xFF241A42),

    background = Color(0xFF0D1524),
    onBackground = Color(0xFFF7F7FC),

    surface = Color(0xFF151E2D),
    onSurface = Color(0xFFF7F7FC),

    surfaceVariant = Color(0xFF1B2638),
    onSurfaceVariant = Color(0xFFAAB4C5),

    outline = Color(0xFF354158),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6C4FF8),
    onPrimary = Color.White,

    primaryContainer = Color(0xFFF0EBFF),
    onPrimaryContainer = Color(0xFF291469),

    secondary = Color(0xFF625B71),
    onSecondary = Color.White,

    background = Color(0xFFF8F8FC),
    onBackground = Color(0xFF171C35),

    surface = Color.White,
    onSurface = Color(0xFF171C35),

    surfaceVariant = Color(0xFFF3F3F9),
    onSurfaceVariant = Color(0xFF70758A),

    outline = Color(0xFFE0E1EA),

    error = Color(0xFFBA1A1A),
    onError = Color.White
)

@Composable
fun TaskFlowTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val systemDarkTheme =
        isSystemInDarkTheme()

    val useDarkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> systemDarkTheme
    }

    val context = LocalContext.current

    val colorScheme = when {
        dynamicColor &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {

            if (useDarkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        useDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}