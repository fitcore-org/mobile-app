package com.example.fitcore.application.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitcore.application.di.ViewModelFactoryProvider
import com.example.fitcore.application.viewmodel.DashboardUiState
import com.example.fitcore.application.viewmodel.DashboardViewModel
import com.example.fitcore.application.viewmodel.WorkoutUiState
import com.example.fitcore.application.viewmodel.WorkoutViewModel
import com.example.fitcore.domain.model.User
import com.example.fitcore.domain.model.Workout

@Composable
fun WorkoutMainScreen(user: User) {
    val dashboardViewModel: DashboardViewModel = viewModel(
        factory = ViewModelFactoryProvider.provideDashboardViewModelFactory()
    )
    val workoutViewModel: WorkoutViewModel = viewModel(
        factory = ViewModelFactoryProvider.provideWorkoutViewModelFactory(user.id)
    )

    val dashboardState by dashboardViewModel.uiState.collectAsState()
    val workoutState by workoutViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Seção de Plano de Treino
        Text(
            text = "Seu Plano de Treino",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Status do Plano
        TrainingPlanStatusCard(dashboardState = dashboardState)

        Spacer(modifier = Modifier.height(24.dp))

        // Lista de Treinos
        Text(
            text = "Exercícios Disponíveis",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        WorkoutsList(workoutState = workoutState)
    }
}

@Composable
fun TrainingPlanStatusCard(dashboardState: DashboardUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            when (val state = dashboardState) {
                is DashboardUiState.Loading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Verificando sua ficha de treino...")
                    }
                }

                is DashboardUiState.Success -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Você tem um plano ativo!",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.trainingPlan.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { /* TODO: Navegar para detalhes do plano */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Ver Detalhes do Plano")
                        }
                    }
                }

                is DashboardUiState.NoPlan -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Nenhuma ficha de treino ativa",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { /* TODO: Navegar para explorar treinos */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Explorar Treinos Padrão")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { /* TODO: Navegar para o chat */ },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Text("Fale com um Personal")
                        }
                    }
                }

                is DashboardUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutsList(workoutState: WorkoutUiState) {
    when (val state = workoutState) {
        is WorkoutUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is WorkoutUiState.Success -> {
            WorkoutList(workouts = state.workouts)
        }

        is WorkoutUiState.Error -> {
            Text(
                text = state.message,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun WorkoutList(workouts: List<Workout>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(workouts) { workout ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(workout.title, style = MaterialTheme.typography.titleMedium)
                    Text(workout.description, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
