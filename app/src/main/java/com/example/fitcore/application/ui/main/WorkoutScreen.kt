package com.example.fitcore.application.ui.main

// Imports adicionados
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
// Fim dos imports adicionados

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

    // 1. Definição do gradiente
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0a0a0a),
            Color(0xFF1a1a1a),
            Color(0xFF2d4a35),
            Color(0xFF1f3a26),
            Color(0xFF0d0f0d)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            // 2. Aplicação do fundo gradiente
            .background(brush = gradientBrush)
            .padding(16.dp)
    ) {
        // Seção de Plano de Treino
        Text(
            text = "Seu Plano de Treino",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp),
            // 3. Cor do texto ajustada para legibilidade
            color = Color.White
        )

        // Status do Plano
        TrainingPlanStatusCard(dashboardState = dashboardState)

        Spacer(modifier = Modifier.height(24.dp))

        // Lista de Treinos
        Text(
            text = "Exercícios Disponíveis",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 12.dp),
            // 3. Cor do texto ajustada para legibilidade
            color = Color.White
        )

        WorkoutsList(workoutState = workoutState)
    }
}

@Composable
fun TrainingPlanStatusCard(dashboardState: DashboardUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            // Cor do card ajustada para combinar com o fundo escuro
            containerColor = Color.White.copy(alpha = 0.1f)
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
                        CircularProgressIndicator(color = Color.White)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Verificando sua ficha de treino...",
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                is DashboardUiState.Success -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Você tem um plano ativo!",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.trainingPlan.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { /* TODO: Navegar para detalhes do plano */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2d4a35) // Cor do botão ajustada
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
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { /* TODO: Navegar para explorar treinos */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2d4a35)
                            )
                        ) {
                            Text("Explorar Treinos Padrão")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { /* TODO: Navegar para o chat */ },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
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
                CircularProgressIndicator(color = Color.White)
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
                    // Cor do card ajustada para combinar
                    containerColor = Color.White.copy(alpha = 0.05f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = workout.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = workout.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}