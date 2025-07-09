package com.example.fitcore.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Sua nova paleta de cores
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFF8F3),      // SEASHELL - Para textos e elementos principais
    secondary = Color(0xFF7B896F),    // RESEDA GREEN - Para textos secundários
    background = Color(0xFF1F3A26),   // DARK GREEN - Fundo principal
    surface = Color(0xFF0D0F0D),      // NIGHT BLACK - Cor para o cartão de login
    onPrimary = Color(0xFF0D0F0D),    // NIGHT BLACK - Texto em cima de botões brancos
    onSecondary = Color(0xFFFFF8F3),  // SEASHELL
    onBackground = Color(0xFFFFF8F3), // SEASHELL - Texto no fundo principal
    onSurface = Color(0xFFFFF8F3),    // SEASHELL - Texto nos cartões
    error = Color(0xFFCF6679)         // Cor de erro padrão para temas escuros
)

@Composable
fun FitcoreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
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
        typography = Typography, // Usa a nova tipografia Montserrat
        content = content
    )
}