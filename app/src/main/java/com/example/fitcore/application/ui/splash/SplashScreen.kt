package com.example.fitcore.application.ui.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitcore.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToLogin: () -> Unit) {
    // --- ANIMAÇÕES ---
    val infiniteTransition = rememberInfiniteTransition(label = "FitCoreGlow")
    val glow by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 25f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "Glow"
    )
    val textOffsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "TextOffsetY"
    )

    var startAnimation by remember { mutableStateOf(false) }
    val animationDuration = 800

    val contentAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 1f,
        animationSpec = tween(durationMillis = animationDuration),
        label = "ContentAlpha"
    )
    val logoOffsetY by animateFloatAsState(
        targetValue = if (startAnimation) -200f else 0f,
        animationSpec = tween(durationMillis = animationDuration),
        label = "LogoOffsetY"
    )
    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 0.5f else 1f,
        animationSpec = tween(durationMillis = animationDuration),
        label = "LogoScale"
    )

    LaunchedEffect(startAnimation) {
        if (startAnimation) {
            delay(animationDuration.toLong())
            onNavigateToLogin()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fundo_treino),
            contentDescription = "Imagem de fundo de um atleta treinando",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 48.dp)
                .graphicsLayer { alpha = contentAlpha },
            // MUDANÇA 1: Alinhar todo o conteúdo desta coluna à esquerda
            horizontalAlignment = Alignment.Start
        ) {
            Column(
                // O alinhamento aqui já estava correto
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Spacer(modifier = Modifier.height(300.dp))
                Image(
                    painter = painterResource(id = R.drawable.fitcore_logo),
                    contentDescription = "FitCore Logo",
                    modifier = Modifier
                        .size(70.dp)
                        // MUDANÇA 2: Remover o alinhamento centralizado para que o logo fique à esquerda
                        .align(Alignment.CenterHorizontally)
                        .graphicsLayer {
                            scaleX = logoScale
                            scaleY = logoScale
                            translationY = logoOffsetY
                        }
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "Bem-vindo ao",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "FitCore",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 52.sp,
                    style = MaterialTheme.typography.displayLarge.copy(
                        shadow = Shadow(
                            color = MaterialTheme.colorScheme.primary,
                            blurRadius = glow
                        )
                    ),
                    modifier = Modifier.graphicsLayer {
                        translationY = textOffsetY
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Seu espaço de evolução! Aqui, cada treino é um passo rumo à sua melhor versão. Disciplina, foco e constância transformam metas em conquistas. Vamos juntos nessa jornada?",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
            Button(
                onClick = { startAnimation = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    "COMEÇAR",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}