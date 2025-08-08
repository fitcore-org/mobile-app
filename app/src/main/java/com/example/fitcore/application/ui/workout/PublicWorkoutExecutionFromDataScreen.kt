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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.fitcore.application.di.ViewModelFactoryProvider
import com.example.fitcore.application.viewmodel.WorkoutExecutionFromDataUiState
import com.example.fitcore.application.viewmodel.WorkoutExecutionFromDataViewModel
import com.example.fitcore.application.state.WorkoutSelectionManager
import com.example.fitcore.application.ui.components.ExerciseAnimationImages
import com.example.fitcore.application.ui.components.PulsingTimer
import com.example.fitcore.application.ui.components.MotivationalBadge
import com.example.fitcore.application.ui.components.CompletionAnimation
import com.example.fitcore.application.ui.workout.WorkoutProgressSection
import com.example.fitcore.application.ui.workout.WorkoutStateSection
import com.example.fitcore.application.ui.workout.WorkoutCompletedSection
import com.example.fitcore.application.ui.workout.NavigationControls
import com.example.fitcore.domain.model.WorkoutSession
import com.example.fitcore.domain.model.WorkoutSessionState
import com.example.fitcore.domain.model.WorkoutItem

// Gradiente de fundo seguindo a paleta da aplicação
private val backgroundGradientBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xFF0a0a0a),
        Color(0xFF1a1a1a),
        Color(0xFF2d4a35),
        Color(0xFF1f3a26),
        Color(0xFF0d0f0d)
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicWorkoutExecutionFromDataScreen(
    onNavigateBack: () -> Unit
) {
    val selectedWorkout = WorkoutSelectionManager.getSelectedWorkout()
    
    if (selectedWorkout == null) {
        // Se não há treino selecionado, volta para a tela anterior
        LaunchedEffect(Unit) {
            onNavigateBack()
        }
        return
    }

    val viewModel: WorkoutExecutionFromDataViewModel = viewModel(
        factory = ViewModelFactoryProvider.provideWorkoutExecutionFromDataViewModelFactory(selectedWorkout)
    )

    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Executar Treino",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradientBrush)
                .padding(paddingValues)
        ) {
            when (state) {
                is WorkoutExecutionFromDataUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFF22c55e),
                                strokeWidth = 4.dp,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = "Carregando treino...",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

                is WorkoutExecutionFromDataUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.05f)
                            ),
                            border = BorderStroke(1.dp, Color(0xFFef4444).copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Icon(
                                    Icons.Default.Error,
                                    contentDescription = null,
                                    tint = Color(0xFFef4444),
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    text = (state as WorkoutExecutionFromDataUiState.Error).message,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }

                is WorkoutExecutionFromDataUiState.Success -> {
                    WorkoutExecutionContentFromData(
                        session = (state as WorkoutExecutionFromDataUiState.Success).session,
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
fun WorkoutExecutionContentFromData(
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
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Progress Bar com espaçamento superior
        Spacer(modifier = Modifier.height(8.dp))
        WorkoutProgressSection(session)
        
        if (session.isCompleted) {
            Spacer(modifier = Modifier.height(8.dp))
            WorkoutCompletedSection()
        } else {
            session.currentWorkoutItem?.let { workoutItem ->
                // Informações do exercício atual sempre visíveis
                CompactExerciseInfoSectionFromData(workoutItem, session.currentSet, session.state)
                
                // Estado atual e timer
                WorkoutStateSection(
                    session = session,
                    onStartExercise = onStartExercise,
                    onFinishSet = onFinishSet,
                    onSkipRest = onSkipRest,
                    onPauseTimer = onPauseTimer,
                    onResumeTimer = onResumeTimer
                )
                
                // Controles de navegação
                NavigationControls(
                    session = session,
                    onPreviousExercise = onPreviousExercise,
                    onNextExercise = onNextExercise
                )
            }
        }
        
        // Espaçamento inferior para scroll
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun CompactExerciseInfoSectionFromData(workoutItem: WorkoutItem, currentSet: Int, sessionState: WorkoutSessionState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.08f)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Cabeçalho com nome do exercício
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = workoutItem.exercise.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                
                // Badge do grupo muscular usando o enum MuscleGroup
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF22c55e).copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    val muscleGroupEnum = com.example.fitcore.domain.model.MuscleGroup.fromApiValue(workoutItem.exercise.muscleGroup)
                    Text(
                        text = "${muscleGroupEnum.emoji} ${muscleGroupEnum.displayName}",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF22c55e),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Chips de informação com design melhorado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CompactInfoChipFromData("Séries", workoutItem.sets)
                CompactInfoChipFromData("Reps", workoutItem.reps)
                CompactInfoChipFromData("Descanso", "${workoutItem.restSeconds}s")
            }
            
            Spacer(modifier = Modifier.height(20.dp))

            // Sempre mostrar a animação do exercício ou placeholder seguro
            val mediaUrl = workoutItem.exercise?.mediaUrl
            val mediaUrl2 = workoutItem.exercise?.mediaUrl2
            val exerciseName = workoutItem.exercise?.name ?: "Exercício"
            if (!mediaUrl.isNullOrBlank() || !mediaUrl2.isNullOrBlank()) {
                ExerciseAnimationImages(
                    mediaUrl = mediaUrl,
                    mediaUrl2 = mediaUrl2,
                    exerciseName = exerciseName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    isAnimating = sessionState == WorkoutSessionState.EXERCISING
                )
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF11291b)
                    ),
                    border = BorderStroke(1.dp, Color(0xFF22c55e).copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.FitnessCenter,
                                contentDescription = null,
                                tint = Color(0xFF22c55e),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = exerciseName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF22c55e),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            
            // Observação com design aprimorado
            workoutItem.observation?.let { observation ->
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF22c55e).copy(alpha = 0.15f)
                    ),
                    border = BorderStroke(1.dp, Color(0xFF22c55e).copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = Color(0xFF22c55e),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = observation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CompactInfoChipFromData(label: String, value: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF22c55e).copy(alpha = 0.15f)
        ),
        border = BorderStroke(1.dp, Color(0xFF22c55e).copy(alpha = 0.3f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF22c55e),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
