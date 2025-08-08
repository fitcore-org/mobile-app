package com.example.fitcore.application.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitcore.application.di.ViewModelFactoryProvider
import com.example.fitcore.application.state.WorkoutSelectionManager
import com.example.fitcore.application.viewmodel.WorkoutViewModel
import com.example.fitcore.application.viewmodel.WorkoutUiState
import com.example.fitcore.domain.model.*

@Composable
fun WorkoutMainScreen(
    user: User,
    onNavigateToWorkoutExecution: () -> Unit = {}
) {
    val workoutViewModel: WorkoutViewModel = viewModel(
        factory = ViewModelFactoryProvider.provideWorkoutViewModelFactory(user.id.toInt())
    )
    val workoutState by workoutViewModel.uiState.collectAsState()

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0D100D), // Tom mais escuro no topo
            Color(0xFF1F3A26), // Verde escuro no meio
            Color(0xFF0A0A0A)  // Preto na base
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
            .padding(16.dp)
    ) {
        // Título da tela com mais destaque
        Text(
            text = "Treinos Disponíveis",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp, top = 8.dp),
            color = Color.White
        )

        WorkoutList(
            workoutState = workoutState,
            onWorkoutClick = { workout ->
                WorkoutSelectionManager.setSelectedWorkout(workout)
                onNavigateToWorkoutExecution()
            }
        )
    }
}

@Composable
fun WorkoutList(
    workoutState: WorkoutUiState,
    onWorkoutClick: (PublicWorkout) -> Unit
) {
    when (val state = workoutState) {
        is WorkoutUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF4ade80))
            }
        }
        is WorkoutUiState.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (state.personalizedWorkouts.isNotEmpty()) {
                    item {
                        Text(
                            text = "Seus Treinos Personalizados",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(state.personalizedWorkouts) { workout ->
                        WorkoutCard(
                            workout = workout.toPublicWorkout(),
                            onClick = { onWorkoutClick(workout.toPublicWorkout()) }
                        )
                    }
                }

                item {
                    Text(
                        text = "Treinos Disponíveis",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(state.publicWorkouts) { workout ->
                    WorkoutCard(
                        workout = workout.toPublicWorkout(),
                        onClick = { onWorkoutClick(workout.toPublicWorkout()) }
                    )
                }
            }
        }
        is WorkoutUiState.Error -> {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun WorkoutCard(
    workout: PublicWorkout,
    onClick: () -> Unit
) {
    // Cores com a nova identidade visual
    val cardBackgroundColor = Color(0xFF101D10)
    val neonGreenColor = Color(0xFF4ade80)
    val lightGrayColor = Color.White.copy(alpha = 0.7f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = cardBackgroundColor
        ),
        border = BorderStroke(1.dp, neonGreenColor.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = workout.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = neonGreenColor
                )
                Text(
                    text = workout.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = lightGrayColor
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${workout.items.size} exercícios",
                        style = MaterialTheme.typography.bodySmall,
                        color = lightGrayColor
                    )
                    Text(
                        text = estimateWorkoutDuration(workout),
                        style = MaterialTheme.typography.bodySmall,
                        color = lightGrayColor
                    )
                }
            }
            Icon(
                imageVector = Icons.Filled.PlayCircle,
                contentDescription = "Iniciar Treino",
                tint = neonGreenColor,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}
