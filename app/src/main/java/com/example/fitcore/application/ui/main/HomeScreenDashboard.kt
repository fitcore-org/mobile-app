package com.example.fitcore.application.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fitcore.domain.model.User
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.*

/**
 * Tela principal (Dashboard) que exibe uma saudação ao usuário, sua foto de perfil,
 * um calendário simples, treino sugerido, uma mensagem motivacional e ações rápidas.
 */

data class QuickAction(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color,
    val onClick: () -> Unit
)

@Composable
fun HomeScreen(
    user: User,
    onNavigateToWorkoutExecution: () -> Unit = {},
    onNavigateToExerciseLibrary: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // O degradê é aplicado no Column principal, que serve como container para toda a tela.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Saudação com foto de perfil
        GreetingProfileSection(user = user)

        Spacer(modifier = Modifier.height(24.dp))

        // Calendário Simples
        SimpleCalendarSection()

        Spacer(modifier = Modifier.height(24.dp))

        // Card de Treino Sugerido - Agora com maior destaque
        ChestWorkoutCard(onStartWorkout = onNavigateToWorkoutExecution)

        Spacer(modifier = Modifier.height(24.dp))

        // Mensagem Motivacional
        MotivationalMessage()

        Spacer(modifier = Modifier.height(24.dp))

        // Cartões de Ação Rápida
        QuickActionsSection(onNavigateToExerciseLibrary = onNavigateToExerciseLibrary)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun GreetingProfileSection(user: User) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        WelcomeText(user = user)
        ProfilePicture(profileUrl = "https://placehold.co/100x100/E0E0E0/333?text=Foto")
    }
}

@Composable
fun WelcomeText(user: User) {
    val currentHour = LocalTime.now().hour
    val greeting = when {
        currentHour < 12 -> "Bom dia"
        currentHour < 18 -> "Boa tarde"
        else -> "Boa noite"
    }

    Column {
        Text(
            text = "$greeting,",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = user.name.split(" ").first(),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ProfilePicture(profileUrl: String) {
    AsyncImage(
        model = profileUrl,
        contentDescription = "Foto de perfil",
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
        contentScale = ContentScale.Crop,
        placeholder = rememberVectorPainter(image = Icons.Default.Person),
        error = rememberVectorPainter(image = Icons.Default.Person)
    )
}

@Composable
fun SimpleCalendarSection() {
    val today = LocalDate.now()
    val locale = Locale("pt", "BR")
    val dayOfWeek = today.dayOfWeek.getDisplayName(TextStyle.FULL, locale)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
    val dayOfMonth = today.dayOfMonth
    val month = today.month.getDisplayName(TextStyle.FULL, locale)

    val dateText = "$dayOfWeek, $dayOfMonth de $month"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.CalendarToday,
                contentDescription = "Calendário",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = dateText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ChestWorkoutCard(onStartWorkout: () -> Unit) {
    // SUGESTÃO 1: Card de Ação Principal com maior destaque visual.
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Treino de Peito",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "4 exercícios • 45-60 min",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    )
                }

                Icon(
                    Icons.Default.FitnessCenter,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // SUGESTÃO 2: Indicador de progresso para engajamento.
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Progresso do Treino",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "50%",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = 0.5f, // Valor mockado
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    trackColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onStartWorkout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Continuar Treino",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun MotivationalMessage() {
    val motivationalMessages = listOf(
        "Cada treino é um passo em direção ao seu objetivo! 💪",
        "Você está mais forte do que ontem! 🔥",
        "Consistência é a chave para o sucesso! ⚡",
        "Seu corpo agradece por cada movimento! 🏃‍♂️",
        "Transforme suor em conquista! 🏆"
    )

    val randomMessage = remember { motivationalMessages.random() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = randomMessage,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
fun QuickActionsSection(onNavigateToExerciseLibrary: () -> Unit = {}) {
    // SUGESTÃO 3: Cores das ações unificadas com o tema.
    val quickActions = listOf(
        QuickAction(
            title = "Biblioteca",
            subtitle = "Aprenda novos movimentos",
            icon = Icons.Default.MenuBook,
            color = MaterialTheme.colorScheme.secondary,
            onClick = onNavigateToExerciseLibrary
        ),
        QuickAction(
            title = "Pagamento",
            subtitle = "Plano ativo até 25/08",
            icon = Icons.Default.CreditCard,
            color = Color(0xFF4CAF50), // Verde mantido para status de "sucesso"
            onClick = { /* TODO: Navegar para pagamento */ }
        )
    )

    Text(
        text = "Ações Rápidas",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 12.dp)
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        quickActions.forEach { action ->
            QuickActionCard(action = action)
        }
    }
}

@Composable
fun QuickActionCard(action: QuickAction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        onClick = action.onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(action.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    action.icon,
                    contentDescription = action.title,
                    tint = action.color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = action.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = action.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Ir para ${action.title}",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}
