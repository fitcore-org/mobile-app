package com.example.fitcore.application.ui.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitcore.application.di.ViewModelFactoryProvider
import com.example.fitcore.application.ui.components.ExerciseAnimationImages
import com.example.fitcore.application.viewmodel.ExerciseLibraryUiState
import com.example.fitcore.application.viewmodel.ExerciseLibraryViewModel
import com.example.fitcore.domain.model.Exercise
import com.example.fitcore.domain.model.MuscleGroup

// 1. Gradiente definido no topo do arquivo para ser reutilizado
private val gradientBrush = Brush.linearGradient(
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
fun ExerciseDetailScreen(
    exerciseId: String,
    onNavigateBack: () -> Unit,
    viewModel: ExerciseLibraryViewModel = viewModel(
        factory = ViewModelFactoryProvider.provideExerciseLibraryViewModelFactory()
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    var exercise by remember { mutableStateOf<Exercise?>(null) }

    LaunchedEffect(exerciseId) {
        viewModel.loadExercises()
    }

    LaunchedEffect(uiState) {
        if (uiState is ExerciseLibraryUiState.Success) {
            exercise = (uiState as ExerciseLibraryUiState.Success).exercises.find { it.id == exerciseId }
        }
    }

    exercise?.let { currentExercise ->
        ExerciseDetailContent(
            exercise = currentExercise,
            onNavigateBack = onNavigateBack
        )
    } ?: run {
        // Estado de Loading ou Erro com fundo gradiente
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBrush), // Fundo aplicado aqui
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is ExerciseLibraryUiState.Loading -> {
                    CircularProgressIndicator(color = Color.White)
                }
                is ExerciseLibraryUiState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Erro ao carregar exercício", color = Color.White)
                        Button(onClick = onNavigateBack) {
                            Text("Voltar")
                        }
                    }
                }
                else -> {
                    Text("Exercício não encontrado", color = Color.White)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExerciseDetailContent(
    exercise: Exercise,
    onNavigateBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    // 2. Fundo aplicado à tela inteira
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        // Top Bar com cores ajustadas
        TopAppBar(
            title = {
                Text(
                    exercise.name,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Animação do exercício (Card com cor ajustada)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black.copy(alpha = 0.2f)
                )
            ) {
                if (exercise.mediaUrl != null && exercise.mediaUrl2 != null) {
                    ExerciseAnimationImages(
                        mediaUrl = exercise.mediaUrl,
                        mediaUrl2 = exercise.mediaUrl2,
                        exerciseName = exercise.name,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Image,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color.White.copy(alpha = 0.6f)
                            )
                            Text(
                                text = "Imagem não disponível",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }

            // Seções de informação com cores ajustadas
            ExerciseInfoSection(exercise = exercise)
            ExerciseDescriptionSection(description = exercise.description)
            SafetyTipsSection()
        }
    }
}

@Composable
fun ExerciseInfoSection(exercise: Exercise) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Informações do Exercício",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                InfoItem(
                    icon = Icons.Default.FitnessCenter,
                    label = "Grupo Muscular",
                    value = MuscleGroup.fromApiValue(exercise.muscleGroup).displayName,
                    color = Color(0xFF10B981) // Verde vibrante
                )
                InfoItem(
                    icon = Icons.Default.Build,
                    label = "Equipamento",
                    value = exercise.equipment,
                    color = Color(0xFF6366F1) // Roxo vibrante
                )
            }
        }
    }
}

@Composable
fun InfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(120.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = Color.White // Ajustado
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.7f), // Ajustado
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ExerciseDescriptionSection(description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Como Executar",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
                color = Color.White
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.4,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
fun SafetyTipsSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            // Cor do card de aviso ajustada para o tema
            containerColor = Color(0xFFE65100).copy(alpha = 0.15f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFE65100), // Laranja
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Dicas de Segurança",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE65100) // Laranja
                )
            }
            val safetyTips = listOf(
                "Sempre aqueça antes de começar",
                "Mantenha uma postura adequada",
                "Respire corretamente durante o movimento",
                "Pare se sentir dor ou desconforto",
                "Comece com pesos leves e aumente gradualmente"
            )
            safetyTips.forEach { tip ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Text(
                        text = "• ",
                        color = Color(0xFFE65100),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = tip,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFE65100).copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}