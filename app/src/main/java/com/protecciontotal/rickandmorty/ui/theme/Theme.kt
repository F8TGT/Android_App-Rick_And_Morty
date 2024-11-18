package com.protecciontotal.rickandmorty.ui.theme

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

// Light mode colors
val LimeGreen = Color(0xFF97CE4C)
val CyanBlue = Color(0xFF00BCD4)
val SoftWhite = Color(0xFFF8F9FA)
val SoftBlack = Color(0xFF2E2E2E)
val MediumGray = Color(0xFFB0B0B0)
val LightGray = Color(0xFFE0E0E0)

// Dark mode colors
val DarkBackground = Color(0xFF121212)

private val LightColorScheme = lightColorScheme(
    primary = LimeGreen,
    secondary = CyanBlue,
    background = LightGray,
    surface = SoftWhite,
    onPrimary = SoftBlack,
    onSecondary = SoftBlack,
    onBackground = SoftBlack,
    onSurface = SoftBlack
)

private val DarkColorScheme = darkColorScheme(
    primary = LimeGreen,
    secondary = CyanBlue,
    background = DarkBackground,
    surface = SoftBlack,
    onPrimary = LightGray,
    onSecondary = LightGray,
    onBackground = MediumGray,
    onSurface = MediumGray
)

@Composable
fun RickAndMortyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}