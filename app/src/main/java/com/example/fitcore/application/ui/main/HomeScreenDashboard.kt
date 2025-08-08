package com.example.fitcore.application.ui.main

import androidx.compose.foundation.BorderStroke
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.fitcore.application.di.ViewModelFactoryProvider
import com.example.fitcore.application.viewmodel.DashboardUiState
import com.example.fitcore.application.viewmodel.DashboardViewModel
import com.example.fitcore.domain.model.User
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.*

data class QuickAction(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color,
    val onClick: () -> Unit
)

@Composable
fun HomeScreen(
    initialUser: User,
    onNavigateToWorkoutExecution: () -> Unit = {},
    onNavigateToExerciseLibrary: () -> Unit = {}
) {
    // 1. Usa o DashboardViewModel para buscar os dados mais recentes.
    val dashboardViewModel: DashboardViewModel = viewModel(
        factory = ViewModelFactoryProvider.provideDashboardViewModelFactory(initialUser) // Assumindo que você tem uma factory para ele
    )
    val uiState by dashboardViewModel.uiState.collectAsState()

    // 2. A tela reage ao estado (Carregando, Erro, Sucesso)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0D100D), Color(0xFF1F3A26), Color(0xFF0A0A0A))
                )
            )
    ) {
        when (val state = uiState) {
            is DashboardUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF4ade80)
                )
            }
            is DashboardUiState.Error -> {
                Text(
                    text = state.message,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }
            is DashboardUiState.Success -> {
                // 3. Uma vez que os dados chegam, o conteúdo da tela é exibido com os dados corretos.
                HomeScreenContent(
                    user = state.user,
                    onNavigateToExerciseLibrary = onNavigateToExerciseLibrary
                )
            }
        }
    }
}

@Composable
fun HomeScreenContent(
    user: User,
    onNavigateToExerciseLibrary: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        GreetingProfileSection(user = user)
        Spacer(modifier = Modifier.height(24.dp))
        SimpleCalendarSection()
        Spacer(modifier = Modifier.height(24.dp))
        WeeklyProgressSection()
        Spacer(modifier = Modifier.height(24.dp))
        MotivationalMessage()
        Spacer(modifier = Modifier.height(24.dp))
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
        // 4. A correção final: usa a foto do usuário ou um placeholder se não houver foto.
        ProfilePicture(
            profileUrl = user.photoUrl ?: "https://placehold.co/100x100/22c55e/FFFFFF?text=${user.name.firstOrNull()?.uppercase()}&font=sans"
        )
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
            color = Color.White.copy(alpha = 0.7f)
        )
        Text(
            text = user.name.split(" ").first(),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4ade80)
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
            .border(2.dp, Color(0xFF4ade80), CircleShape),
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
        .replaceFirstChar { it.titlecase(locale) }
    val dayOfMonth = today.dayOfMonth
    val month = today.month.getDisplayName(TextStyle.FULL, locale)
    val dateText = "$dayOfWeek, $dayOfMonth de $month"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
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
                tint = Color(0xFF4ade80),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = dateText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
fun WeeklyProgressSection() {
    val today = LocalDate.now().dayOfWeek
    val completedDays = remember { setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY) }
    val weekDays = DayOfWeek.values().sortedBy { it.value }

    Column {
        Text(
            text = "Seu Progresso da Semana",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp),
            color = Color.White
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                weekDays.forEach { day ->
                    DayProgress(
                        day = day,
                        isToday = day == today,
                        isCompleted = day in completedDays
                    )
                }
            }
        }
    }
}

@Composable
fun DayProgress(day: DayOfWeek, isToday: Boolean, isCompleted: Boolean) {
    val locale = Locale("pt", "BR")
    val dayInitial = day.getDisplayName(TextStyle.SHORT, locale).first().uppercase()

    val color = when {
        isCompleted -> Color(0xFF4ade80)
        isToday -> Color(0xFF4ade80)
        else -> Color.White.copy(alpha = 0.3f)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = dayInitial, color = color, fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (isCompleted) color else Color.Transparent)
                .border(
                    width = 2.dp,
                    color = color,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Treino Concluído",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
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
            containerColor = Color(0xFF4ade80).copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFF4ade80).copy(alpha = 0.3f))
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
                color = Color(0xFF4ade80),
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
fun QuickActionsSection(onNavigateToExerciseLibrary: () -> Unit = {}) {
    val quickActions = listOf(
        QuickAction(
            title = "Biblioteca",
            subtitle = "Aprenda novos movimentos",
            icon = Icons.Default.MenuBook,
            color = Color(0xFF4ade80),
            onClick = onNavigateToExerciseLibrary
        ),
        QuickAction(
            title = "Pagamento",
            subtitle = "Plano ativo até 25/08",
            icon = Icons.Default.CreditCard,
            color = Color(0xFF4ade80),
            onClick = { /* TODO: Navegar para pagamento */ }
        )
    )

    Text(
        text = "Ações Rápidas",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 12.dp),
        color = Color.White
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
            containerColor = Color.White.copy(alpha = 0.05f)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
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
                    color = Color.White
                )
                Text(
                    text = action.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Ir para ${action.title}",
                tint = Color.White.copy(alpha = 0.5f)
            )
        }
    }
}