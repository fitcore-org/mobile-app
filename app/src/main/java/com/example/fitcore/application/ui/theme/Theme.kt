package com.example.fitcore.application.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/*
 * Defines the overall Material 3 colour scheme for the Fitcore mobile app.
 *
 * The web front‑end uses a sophisticated palette of deep greens, blacks
 * and vibrant greens for call‑to‑action elements. To replicate that on
 * mobile we override Material 3's dark colour scheme with our own values
 * drawn from Colour.kt. A few extra roles such as tertiary and outline
 * have been chosen to harmonise with our primary/secondary colours. You can
 * adjust these values centrally to fine‑tune the look of the entire app.
 */

private val FitcoreDarkColorScheme: ColorScheme = darkColorScheme(
    primary = GreenPrimary,                 // accent colour for buttons and highlights
    onPrimary = NightBlack,                // text/icon colour on top of the accent
    primaryContainer = GreenSecondary,     // subtle tint used for cards and chips
    onPrimaryContainer = NightBlack,
    secondary = ResedaGreen,               // secondary tint used for less prominent actions
    onSecondary = Seashell,
    secondaryContainer = DarkGreen,        // background tint for secondary components
    onSecondaryContainer = Seashell,
    tertiary = GreenSecondary,             // third accent used for selected indicators
    onTertiary = NightBlack,
    tertiaryContainer = GreenPrimary.copy(alpha = 0.2f),
    onTertiaryContainer = Seashell,
    background = DarkGreen,                // overall app background
    onBackground = Seashell,               // text colour on backgrounds
    surface = NightBlack,                  // card and sheet background
    onSurface = Seashell,                  // text on cards/surfaces
    surfaceVariant = DarkGreen,           // variant for list items etc.
    onSurfaceVariant = Seashell,
    outline = ResedaGreen.copy(alpha = 0.5f),
    error = Color(0xFFCF6679),             // default error colour for dark theme
    onError = NightBlack,
    errorContainer = Color(0xFFBA1A1A),
    onErrorContainer = Seashell
)

@Composable
fun FitcoreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Always use our dark colour scheme for now. If light support is needed later
    // you can add a FitcoreLightColorScheme and branch here accordingly.
    val colorScheme = FitcoreDarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Set the status bar to our background colour for a seamless look
            window.statusBarColor = colorScheme.background.toArgb()
            // Use light or dark status bar icons based on the system setting
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}