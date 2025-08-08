package com.example.fitcore.application.ui.workout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitcore.application.di.ViewModelFactoryProvider
import com.example.fitcore.application.ui.components.ExerciseAnimationImages
import com.example.fitcore.application.ui.components.PulsingTimer
import com.example.fitcore.application.viewmodel.WorkoutExecutionUiState
import com.example.fitcore.application.viewmodel.WorkoutExecutionViewModel
import com.example.fitcore.domain.model.WorkoutItem
import com.example.fitcore.domain.model.WorkoutSession
import com.example.fitcore.domain.model.WorkoutSessionState

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
                title = { Text("Treino em Execução") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
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
                    WorkoutExecutionContentChest(
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
fun WorkoutExecutionContentChest(
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
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        WorkoutProgressSection(session)

        if (session.isCompleted) {
            WorkoutCompletedSection()
        } else {
            session.currentWorkoutItem?.let { workoutItem ->
                WorkoutStateSection(
                    session = session,
                    onStartExercise = onStartExercise,
                    onFinishSet = onFinishSet,
                    onSkipRest = onSkipRest,
                    onPauseTimer = onPauseTimer,
                    onResumeTimer = onResumeTimer
                )

                ExerciseInfoSection(workoutItem, session.state)

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
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0d0f0d)),
        border = BorderStroke(1.dp, Color(0xFF22c55e).copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Exercício ${session.currentExerciseIndex + 1} de ${session.totalExercises}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4ade80)
                )
                Text(
                    text = "${session.progressPercentage.toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF22c55e)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { session.progressPercentage / 100f },
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF22c55e),
                trackColor = Color(0xFF1f3a26).copy(alpha = 0.3f)
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
    Card(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0d0f0d)),
        border = BorderStroke(1.dp, Color(0xFF22c55e).copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            when (session.state) {
                WorkoutSessionState.PREPARATION -> {
                    PreparationSection(
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
                // CORREÇÃO APLICADA AQUI:
                else -> { /* Não renderiza nada para outros estados como NEXT_EXERCISE, COMPLETED, etc. */ }
            }
        }
    }
}

@Composable
fun PreparationSection(currentSet: Int, totalSets: String, onStartEarly: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = Color(0xFF4ade80), modifier = Modifier.size(32.dp))
        Text("Preparação", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color(0xFF4ade80), textAlign = TextAlign.Center)
        Text("Série $currentSet de $totalSets", style = MaterialTheme.typography.titleMedium, color = Color(0xFF4ade80).copy(alpha = 0.8f), textAlign = TextAlign.Center)
        Box(modifier = Modifier.size(120.dp), contentAlignment = Alignment.Center) {
            Text("Pronto?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color(0xFF4ade80), textAlign = TextAlign.Center)
        }
        Button(
            onClick = onStartEarly,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22c55e)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Começar Agora", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
        }
        Text("Prepare-se para dar o seu melhor!", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF4ade80).copy(alpha = 0.7f), textAlign = TextAlign.Center)
    }
}

@Composable
fun ExercisingSection(currentSet: Int, totalSets: String, reps: String, onFinishSet: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF4ade80), modifier = Modifier.size(32.dp))
        Text("Executando", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color(0xFF4ade80), textAlign = TextAlign.Center)
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Série $currentSet de $totalSets", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFF4ade80), textAlign = TextAlign.Center)
            Text("$reps repetições", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = Color(0xFF4ade80), textAlign = TextAlign.Center)
        }
        Button(
            onClick = onFinishSet,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22c55e)),
            shape = RoundedCornerShape(28.dp)
        ) {
            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Série Concluída", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
        }
        Text("Foque na forma correta!", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF4ade80).copy(alpha = 0.7f), textAlign = TextAlign.Center)
    }
}

@Composable
fun RestingSection(timeRemaining: Int, onSkipRest: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(Icons.Default.Timer, contentDescription = null, tint = Color(0xFF4ade80), modifier = Modifier.size(32.dp))
        Text("Descanso", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color(0xFF4ade80), textAlign = TextAlign.Center)
        PulsingTimer(timeRemaining = timeRemaining, totalTime = 60, color = Color(0xFF4ade80), modifier = Modifier.size(120.dp))
        OutlinedButton(
            onClick = onSkipRest,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4ade80)),
            border = BorderStroke(2.dp, Color(0xFF4ade80)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Icon(Icons.Default.SkipNext, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Pular Descanso", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
        }
        Text("Respire fundo e hidrate-se", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF4ade80).copy(alpha = 0.7f), textAlign = TextAlign.Center)
    }
}

@Composable
fun ExerciseInfoSection(workoutItem: WorkoutItem, sessionState: WorkoutSessionState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0d0f0d)),
        border = BorderStroke(1.dp, Color(0xFF22c55e).copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(workoutItem.exercise.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color(0xFF4ade80))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                InfoChip("Séries", workoutItem.sets)
                InfoChip("Reps", workoutItem.reps)
                InfoChip("Descanso", "${workoutItem.restSeconds}s")
            }
            ExerciseAnimationImages(
                mediaUrl = workoutItem.exercise.mediaUrl,
                mediaUrl2 = workoutItem.exercise.mediaUrl2,
                exerciseName = workoutItem.exercise.name,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                isAnimating = sessionState == WorkoutSessionState.EXERCISING
            )
            Text(workoutItem.exercise.description, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
            workoutItem.observation?.let { observation ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF22c55e).copy(alpha = 0.1f)),
                    border = BorderStroke(1.dp, Color(0xFF22c55e).copy(alpha = 0.2f))
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Color(0xFF4ade80), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(observation, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.9f))
                    }
                }
            }
        }
    }
}

@Composable
fun InfoChip(label: String, value: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF22c55e).copy(alpha = 0.15f)),
        border = BorderStroke(1.dp, Color(0xFF22c55e).copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, style = MaterialTheme.typography.bodySmall, color = Color(0xFF4ade80))
            Text(value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color(0xFF4ade80))
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
            enabled = session.currentExerciseIndex > 0,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4ade80)),
            border = BorderStroke(1.dp, Color(0xFF4ade80))
        ) {
            Icon(Icons.Default.SkipPrevious, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Anterior")
        }
        Button(
            onClick = onNextExercise,
            modifier = Modifier.weight(1f),
            enabled = session.currentExerciseIndex < session.totalExercises - 1,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22c55e))
        ) {
            Text("Próximo")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.SkipNext, contentDescription = null)
        }
    }
}