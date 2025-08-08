package com.example.fitcore.application.ui.workout

import androidx.compose.foundation.BorderStroke
// Import necessário para o FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// OptIn para usar a API experimental de Layout
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WorkoutCompletedSection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0d0f0d)),
            border = BorderStroke(1.dp, Color(0xFF22c55e).copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4ade80),
                    modifier = Modifier.size(64.dp)
                )
                Text(
                    text = "Parabéns!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF4ade80),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Treino concluído com sucesso!",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF4ade80).copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0d0f0d)),
            border = BorderStroke(1.dp, Color(0xFF22c55e).copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Conquistas",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF4ade80),
                    fontWeight = FontWeight.Bold
                )

                // Trocamos LazyRow por FlowRow para quebrar a linha automaticamente
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp) // Espaçamento se quebrar a linha
                ) {
                    val achievements = listOf(
                        "🏆 Primeira vez!" to Color(0xFFFFD700),
                        "💪 Força total" to Color(0xFF22c55e),
                        "⭐ Dedicação" to Color(0xFF4ade80)
                    )

                    // Usamos um forEach padrão para adicionar os itens ao FlowRow
                    achievements.forEach { (text, color) ->
                        AchievementBadge(text = text, backgroundColor = color)
                    }
                }
            }
        }
    }
}

@Composable
private fun AchievementBadge(
    text: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor.copy(alpha = 0.1f)),
        border = BorderStroke(1.dp, backgroundColor),
        modifier = modifier,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodySmall,
            color = backgroundColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
    }
}