package com.example.fitcore.application.ui.workout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.fitcore.application.di.ViewModelFactoryProvider
import com.example.fitcore.application.viewmodel.WorkoutExecutionUiState
import com.example.fitcore.application.viewmodel.WorkoutExecutionViewModel
import com.example.fitcore.application.ui.components.ExerciseAnimationImages
import com.example.fitcore.application.ui.components.PulsingTimer
import com.example.fitcore.application.ui.components.MotivationalBadge
import com.example.fitcore.application.ui.components.CompletionAnimation
import com.example.fitcore.domain.model.WorkoutSession
import com.example.fitcore.domain.model.WorkoutSessionState
import com.example.fitcore.domain.model.WorkoutItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutExecutionScreen(
    onNavigateBack: () -> Unit
) {
    val viewModel: WorkoutExecutionViewModel = viewModel(
        factory = ViewModelFactoryProvider.provideWorkoutExecutionViewModelFactory()
    )
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Treino de Peito") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is WorkoutExecutionUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Carregando treino...")
                        }
                    }
                }

                is WorkoutExecutionUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = state.message,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                is WorkoutExecutionUiState.Success -> {
                    WorkoutExecutionContent(
                        session = state.session,
                        onStartExercise = viewModel::startExercise,
                        onFinishSet = viewModel::finishSet,
                        onSkipRest = viewModel::skipRest,
                        onNextExercise = viewModel::nextExercise,
                        onPreviousExercise = viewModel::previousExercise,
                        onPauseTimer = viewModel::pauseTimer,
                        onResumeTimer = viewModel::resumeTimer
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutExecutionContent(
    session: WorkoutSession,
    onStartExercise: () -> Unit,
    onFinishSet: () -> Unit,
    onSkipRest: () -> Unit,
    onNextExercise: () -> Unit,
    onPreviousExercise: () -> Unit,
    onPauseTimer: () -> Unit,
    onResumeTimer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Progress Bar
        WorkoutProgressSection(session)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        if (session.isCompleted) {
            WorkoutCompletedSection()
        } else {
            session.currentWorkoutItem?.let { workoutItem ->
                // Estado atual e timer
                WorkoutStateSection(
                    session = session,
                    onStartExercise = onStartExercise,
                    onFinishSet = onFinishSet,
                    onSkipRest = onSkipRest,
                    onPauseTimer = onPauseTimer,
                    onResumeTimer = onResumeTimer
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Informações do exercício atual
                ExerciseInfoSection(workoutItem, session.currentSet, session.state)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Controles de navegação
                NavigationControls(
                    session = session,
                    onPreviousExercise = onPreviousExercise,
                    onNextExercise = onNextExercise
                )
            }
        }
    }
}

@Composable
fun WorkoutProgressSection(session: WorkoutSession) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Exercício ${session.currentExerciseIndex + 1} de ${session.totalExercises}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${session.progressPercentage.toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            LinearProgressIndicator(
                progress = { session.progressPercentage / 100f },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun WorkoutStateSection(
    session: WorkoutSession,
    onStartExercise: () -> Unit,
    onFinishSet: () -> Unit,
    onSkipRest: () -> Unit,
    onPauseTimer: () -> Unit,
    onResumeTimer: () -> Unit
) {
    // Card com altura consistente e centralização perfeita
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            when (session.state) {
                WorkoutSessionState.PREPARATION -> {
                    PreparationSection(
                        timeRemaining = session.timeRemaining,
                        currentSet = session.currentSet,
                        totalSets = session.currentWorkoutItem?.sets ?: "1",
                        onStartEarly = onStartExercise
                    )
                }
                
                WorkoutSessionState.EXERCISING -> {
                    ExercisingSection(
                        currentSet = session.currentSet,
                        totalSets = session.currentWorkoutItem?.sets ?: "1",
                        reps = session.currentWorkoutItem?.reps ?: "1",
                        onFinishSet = onFinishSet
                    )
                }
                
                WorkoutSessionState.RESTING -> {
                    RestingSection(
                        timeRemaining = session.timeRemaining,
                        onSkipRest = onSkipRest
                    )
                }
                
                else -> { /* Outros estados */ }
            }
        }
    }
}

@Composable
fun PreparationSection(
    timeRemaining: Int,
    currentSet: Int,
    totalSets: String,
    onStartEarly: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Ícone e título centralizados
        Icon(
            Icons.Default.FitnessCenter,
            contentDescription = null,
            tint = Color(0xFF6366F1), // Indigo moderno
            modifier = Modifier.size(32.dp)
        )
        
        Text(
            text = "Preparação",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6366F1),
            textAlign = TextAlign.Center
        )
        
        // Info da série
        Text(
            text = "Série $currentSet de $totalSets",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF6366F1).copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
        
        // Timer
        PulsingTimer(
            timeRemaining = timeRemaining,
            totalTime = 10,
            color = Color(0xFF6366F1),
            modifier = Modifier.size(120.dp)
        )
        
        // Botão
        Button(
            onClick = onStartEarly,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6366F1)
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Icon(
                Icons.Default.PlayArrow, 
                contentDescription = null, 
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Começar Agora",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }
        
        // Dica
        Text(
            text = "Prepare-se para dar o seu melhor!",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6366F1).copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ExercisingSection(
    currentSet: Int,
    totalSets: String,
    reps: String,
    onFinishSet: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Ícone e título
        Icon(
            Icons.Default.Check,
            contentDescription = null,
            tint = Color(0xFF10B981), // Emerald verde
            modifier = Modifier.size(32.dp)
        )
        
        Text(
            text = "Executando",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF10B981),
            textAlign = TextAlign.Center
        )
        
        // Informações principais
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Série $currentSet de $totalSets",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF10B981),
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "$reps repetições",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF10B981),
                textAlign = TextAlign.Center
            )
        }
        
        // Botão de conclusão
        Button(
            onClick = onFinishSet,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF10B981)
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Série Concluída",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }
        
        // Dica
        Text(
            text = "Foque na forma correta!",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF10B981).copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RestingSection(
    timeRemaining: Int,
    onSkipRest: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Ícone e título
        Icon(
            Icons.Default.Timer,
            contentDescription = null,
            tint = Color(0xFFF59E0B), // Amber/Orange profissional
            modifier = Modifier.size(32.dp)
        )
        
        Text(
            text = "Descanso",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFF59E0B),
            textAlign = TextAlign.Center
        )
        
        // Timer
        PulsingTimer(
            timeRemaining = timeRemaining,
            totalTime = 120,
            color = Color(0xFFF59E0B),
            modifier = Modifier.size(120.dp)
        )
        
        // Botão de pular
        OutlinedButton(
            onClick = onSkipRest,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFFF59E0B)
            ),
            border = BorderStroke(2.dp, Color(0xFFF59E0B)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Icon(
                Icons.Default.SkipNext,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Pular Descanso",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }
        
        // Dica
        Text(
            text = "Respire fundo e hidrate-se",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFFF59E0B).copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ExerciseInfoSection(workoutItem: WorkoutItem, currentSet: Int, sessionState: WorkoutSessionState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = workoutItem.exercise.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoChip("Séries", workoutItem.sets)
                InfoChip("Reps", workoutItem.reps)
                InfoChip("Descanso", "${workoutItem.restSeconds}s")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Imagem animada durante exercício, imagens lado a lado nos outros estados
            if (sessionState == WorkoutSessionState.EXERCISING) {
                ExerciseAnimationImages(
                    mediaUrl = workoutItem.exercise.mediaUrl,
                    mediaUrl2 = workoutItem.exercise.mediaUrl2,
                    exerciseName = workoutItem.exercise.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    isAnimating = true
                )
            } else {
                // Imagens lado a lado para preparação e descanso
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    workoutItem.exercise.mediaUrl?.let { url ->
                        AsyncImage(
                            model = url.replace("localhost", "192.168.0.9"),
                            contentDescription = "Exercício posição 1",
                            modifier = Modifier
                                .weight(1f)
                                .height(120.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    
                    workoutItem.exercise.mediaUrl2?.let { url ->
                        AsyncImage(
                            model = url.replace("localhost", "192.168.0.9"),
                            contentDescription = "Exercício posição 2",
                            modifier = Modifier
                                .weight(1f)
                                .height(120.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = workoutItem.exercise.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            workoutItem.observation?.let { observation ->
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = observation,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoChip(label: String, value: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun NavigationControls(
    session: WorkoutSession,
    onPreviousExercise: () -> Unit,
    onNextExercise: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(
            onClick = onPreviousExercise,
            modifier = Modifier.weight(1f),
            enabled = session.currentExerciseIndex > 0
        ) {
            Icon(Icons.Default.SkipPrevious, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Anterior")
        }
        
        Button(
            onClick = onNextExercise,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Próximo")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.SkipNext, contentDescription = null)
        }
    }
}

@Composable
fun WorkoutCompletedSection() {
    var showStats by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(500)
        showStats = true
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Header de celebração
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Ícone de sucesso
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                // Texto principal
                Text(
                    text = "Parabéns!",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = "Treino de peito concluído",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        // Estatísticas do treino
        if (showStats) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Estatísticas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    
                    // Grid de estatísticas 2x2
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            "Exercícios", 
                            "4", 
                            Icons.Default.FitnessCenter,
                            Color(0xFF10B981), // Verde emerald
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            "Tempo", 
                            "~45min", 
                            Icons.Default.Timer,
                            Color(0xFF6366F1), // Indigo
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            "Séries", 
                            "13", 
                            Icons.Default.Repeat,
                            Color(0xFFF59E0B), // Amber
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            "Calorias", 
                            "~280", 
                            Icons.Default.LocalFireDepartment,
                            Color(0xFFEF4444), // Red moderno
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            
            // Badges de conquista
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Conquistas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AchievementBadge("🏆 Primeira vez!", Color(0xFFF59E0B), modifier = Modifier.weight(1f))
                        AchievementBadge("💪 Força total", Color(0xFF10B981), modifier = Modifier.weight(1f))
                        AchievementBadge("⭐ Dedicação", Color(0xFF6366F1), modifier = Modifier.weight(1f))
                    }
                }
            }
            
            // Botão de resultados detalhados
            Button(
                onClick = { /* TODO: Navegar para tela de resultados */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Icon(Icons.Default.Assessment, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Ver Resultados Detalhados",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    label: String, 
    value: String, 
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color,
                textAlign = TextAlign.Center
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = color.copy(alpha = 0.8f),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AchievementBadge(
    text: String,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor.copy(alpha = 0.1f)
        ),
        border = BorderStroke(1.dp, backgroundColor),
        modifier = modifier
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodySmall,
            color = backgroundColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", minutes, secs)
}
