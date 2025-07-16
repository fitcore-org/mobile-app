package com.example.fitcore.application.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fitcore.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    // Controladores de animação para o alfa (fade-in)
    val alpha = remember { Animatable(0f) }

    // Efeito que é disparado quando a tela é carregada
    LaunchedEffect(key1 = true) {
        // Anima o alfa de 0 (invisível) para 1 (visível)
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500) // Duração da animação
        )
        // Espera 1.5 segundos antes de navegar
        delay(1500)
        onTimeout()
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // A imagem e os textos usam o valor do alfa para a animação de fade-in
            Image(
                painter = painterResource(id = R.drawable.fitcore_logo),
                contentDescription = "FitCore Logo",
                modifier = Modifier
                    .size(180.dp)
                    .alpha(alpha.value)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "FitCore",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.alpha(alpha.value)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Sua evolução começa aqui.",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.alpha(alpha.value)
            )
        }
    }
}