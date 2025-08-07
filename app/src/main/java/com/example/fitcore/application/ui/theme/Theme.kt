package com.example.fitcore.application.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Sua nova paleta aplicada ao ColorScheme do Material 3
private val AppColorScheme = darkColorScheme(
    primary = FitCoreGreen,
    onPrimary = FitCoreWhite,
    secondary = FitCoreGreenLight,
    background = FitCoreBackground,
    surface = FitCoreSurface,
    onSurface = FitCoreWhite,
    onBackground = FitCoreWhite,
    error = FitCoreRed,
    onError = FitCoreWhite
    /* Outras cores podem ser definidas aqui se necessário */
)

@Composable
fun FitcoreTheme(
    darkTheme: Boolean = true, // Forçando o tema escuro, pois sua paleta é escura
    content: @Composable () -> Unit
) {
    val colorScheme = AppColorScheme // Usamos nosso esquema de cores definido acima
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}