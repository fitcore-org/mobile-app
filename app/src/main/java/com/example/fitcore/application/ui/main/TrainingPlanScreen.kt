package com.example.fitcore.application.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fitcore.domain.model.TrainingPlan
import com.example.fitcore.domain.model.Workout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingPlanScreen(
    trainingPlan: TrainingPlan,
    navController: NavHostController
) {
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(trainingPlan.name, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                // 2. Cores da TopAppBar ajustadas
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        // 3. Cor do container do Scaffold transparente para o fundo aparecer
        containerColor = Color.Transparent
    ) { paddingValues ->
        // 4. Fundo aplicado ao contêiner principal
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBrush)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
            ) {
                items(trainingPlan.workouts) { workout ->
                    WorkoutCard(
                        workout = workout,
                        onClick = {
                            navController.navigate("workout_detail/${workout.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutCard(workout: Workout, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        // 5. Cores do Card e dos textos ajustadas
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                workout.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                workout.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}