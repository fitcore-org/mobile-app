package com.example.fitcore.application.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ExerciseAnimationImages(
    mediaUrl: String?,
    mediaUrl2: String?,
    exerciseName: String,
    modifier: Modifier = Modifier,
    isAnimating: Boolean = true
) {
    // Estado para controlar se a animação está ativa
    var animationActive by remember { mutableStateOf(isAnimating) }
    
    // Converte localhost para o IP correto da rede
    val convertedUrl = mediaUrl?.replace("localhost", "192.168.0.9")
    val convertedUrl2 = mediaUrl2?.replace("localhost", "192.168.0.9")
    
    // Animação infinita que alterna entre 0f e 1f a cada 1.5 segundos
    val infiniteTransition = rememberInfiniteTransition(label = "exercise_animation")
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = if (animationActive && isAnimating) {
            infiniteRepeatable(
                animation = tween<Float>(
                    durationMillis = 1500, // 1.5 segundos para cada posição
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            )
        } else {
            infiniteRepeatable(
                animation = tween<Float>(durationMillis = 0),
                repeatMode = RepeatMode.Restart
            )
        },
        label = "image_switch"
    )
    
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Determina qual imagem mostrar baseado na animação
        val showFirstImage = if (animationActive && isAnimating) {
            animationProgress < 0.5f
        } else {
            true // Mostra sempre a primeira quando não está animando
        }
        
        if (showFirstImage) {
            // Primeira imagem (posição inicial)
            if (convertedUrl != null) {
                AsyncImage(
                    model = convertedUrl,
                    contentDescription = "$exerciseName - Posição inicial",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                ExercisePlaceholder("Posição 1")
            }
        } else {
            // Segunda imagem (posição final)
            if (convertedUrl2 != null) {
                AsyncImage(
                    model = convertedUrl2,
                    contentDescription = "$exerciseName - Posição final",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                ExercisePlaceholder("Posição 2")
            }
        }
        
        // Controles de animação no canto superior direito
        if (isAnimating) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                ),
                onClick = { animationActive = !animationActive }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (animationActive) "⏸" else "▶",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Demo",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun ExercisePlaceholder(text: String) {
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
