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
    isAnimating: Boolean = true // mantido apenas para compatibilidade
) {
    // Converte localhost para o IP correto do emulador Android
    val convertedUrl = mediaUrl?.replace("localhost", "10.0.2.2")
    val convertedUrl2 = mediaUrl2?.replace("localhost", "10.0.2.2")
    
    // Animação infinita simples que alterna entre as imagens a cada 1.5 segundos
    val infiniteTransition = rememberInfiniteTransition(label = "exercise_animation")
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "image_switch"
    )
    
    Box(
        modifier = modifier.clip(RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Determina qual imagem mostrar baseado na animação
        val showFirstImage = animationProgress < 0.5f
        
        if (showFirstImage) {
            // Primeira imagem (posição inicial)
            if (convertedUrl != null) {
                AsyncImage(
                    model = convertedUrl,
                    contentDescription = "$exerciseName - Posição inicial",
                    modifier = Modifier.fillMaxSize(),
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
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                ExercisePlaceholder("Posição 2")
            }
        }
        
        // Badge "Demo" simplificado
        Card(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
            )
        ) {
            Text(
                text = "Demo",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
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
