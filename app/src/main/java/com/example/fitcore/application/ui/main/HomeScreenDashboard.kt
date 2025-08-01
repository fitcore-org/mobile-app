package com.example.fitcore.application.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitcore.domain.model.User
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

/*
 * The dashboard screen shows the user's upcoming workouts, motivational
 * messages and quick actions. Colours and backgrounds have been updated to
 * harmonise with the dark theme defined in Theme.kt. In particular, the
 * QuickAction cards no longer hardcode arbitrary colours but instead derive
 * their tints from the theme to provide a consistent look across the app.
 */

data class WeekDay(
    val dayOfWeek: String,
    val dayNumber: Int,
    val isToday: Boolean,
    val isWorkoutDay: Boolean,
    val isCompleted: Boolean
)

data class QuickAction(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun HomeScreen(user: User) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Greeting and motivation
        WelcomeSection(user = user)

        Spacer(modifier = Modifier.height(24.dp))

        // Workout streak section
        WorkoutStreakSection()

        Spacer(modifier = Modifier.height(24.dp))

        // Motivational message card
        MotivationalMessage()

        Spacer(modifier = Modifier.height(24.dp))

        // Quick actions section
        QuickActionsSection()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun WelcomeSection(user: User) {
    val currentHour = java.time.LocalTime.now().hour
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
fun WorkoutStreakSection() {
    val weekDays = generateWeekDays()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ofensiva de Treinos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "3 dias",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(weekDays) { day ->
                    WeekDayItem(day = day)
                }
            }
        }
    }
}

@Composable
fun WeekDayItem(day: WeekDay) {
    val backgroundColor = when {
        day.isCompleted -> MaterialTheme.colorScheme.primary
        day.isToday -> MaterialTheme.colorScheme.secondary
        day.isWorkoutDay -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        else -> MaterialTheme.colorScheme.surface
    }

    val contentColor = when {
        day.isCompleted -> MaterialTheme.colorScheme.onPrimary
        day.isToday -> MaterialTheme.colorScheme.onSecondary
        else -> MaterialTheme.colorScheme.onSurface
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(50.dp)
    ) {
        Text(
            text = day.dayOfWeek,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            if (day.isCompleted) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Treino concluído",
                    tint = contentColor,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = day.dayNumber.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
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
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.tertiaryContainer,
                            MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.8f)
                        )
                    )
                )
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = randomMessage,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
fun QuickActionsSection() {
    val quickActions = listOf(
        QuickAction(
            title = "Pagamento",
            subtitle = "Plano ativo até 25/08",
            icon = Icons.Default.CreditCard
        ) { /* TODO: Navegar para pagamento */ },

        QuickAction(
            title = "Check-in",
            subtitle = "Marcar presença",
            icon = Icons.Default.LocationOn
        ) { /* TODO: Fazer check-in */ },

        QuickAction(
            title = "Progresso",
            subtitle = "Ver evolução",
            icon = Icons.Default.TrendingUp
        ) { /* TODO: Ver progresso */ }
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
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon container uses theme primary colour for its tint instead of arbitrary colours
            val iconBackground = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            val iconTint = MaterialTheme.colorScheme.primary
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    action.icon,
                    contentDescription = action.title,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
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

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Ir para ${action.title}",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

// Helper function to generate week days for the streak section
private fun generateWeekDays(): List<WeekDay> {
    val today = LocalDate.now()
    val startOfWeek = today.with(java.time.DayOfWeek.MONDAY)
    return (0..6).map { dayOffset ->
        val date = startOfWeek.plusDays(dayOffset.toLong())
        WeekDay(
            dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            dayNumber = date.dayOfMonth,
            isToday = date == today,
            isWorkoutDay = listOf(1, 3, 5).contains(date.dayOfWeek.value), // Seg, Qua, Sex
            isCompleted = date.isBefore(today) && listOf(1, 3, 5).contains(date.dayOfWeek.value)
        )
    }
}