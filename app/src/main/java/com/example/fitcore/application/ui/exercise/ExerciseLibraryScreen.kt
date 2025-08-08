package com.example.fitcore.application.ui.exercise

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border // NOVO: Import para o modifier de borda
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.fitcore.application.di.ViewModelFactoryProvider
import com.example.fitcore.application.viewmodel.ExerciseLibraryViewModel
import com.example.fitcore.application.viewmodel.ExerciseLibraryUiState
import com.example.fitcore.domain.model.Exercise
import com.example.fitcore.domain.model.MuscleGroup

// --- PALETA DE CORES E DEGRADÊ ---

private val backgroundGradientBrush = Brush.linearGradient(
    colorStops = arrayOf(
        0.00f to Color(0xFF0A0A0A),
        0.25f to Color(0xFF1A1A1A),
        0.50f to Color(0xFF2D4A35),
        0.75f to Color(0xFF1F3A26),
        1.00f to Color(0xFF0D0F0D)
    )
)

private val accentColor = Color(0xFF6EE7B7) // Verde menta claro
private val accentLightColor = accentColor.copy(alpha = 0.1f)
private val accentBorderColor = accentColor.copy(alpha = 0.3f)

private val secondaryAccentColor = Color(0xFF818CF8) // Lavanda para contraste
private val secondaryLightColor = secondaryAccentColor.copy(alpha = 0.1f)
private val secondaryBorderColor = secondaryAccentColor.copy(alpha = 0.3f)

private val errorColor = Color(0xFFF87171) // Vermelho claro para erros
private val errorLightColor = errorColor.copy(alpha = 0.1f)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseLibraryScreen(
    onNavigateBack: () -> Unit,
    onExerciseClick: (Exercise) -> Unit = {},
    viewModel: ExerciseLibraryViewModel = viewModel(
        factory = ViewModelFactoryProvider.Factory
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadExercises()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradientBrush)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Biblioteca", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        scrolledContainerColor = Color(0xFF0A0A0A).copy(alpha = 0.9f)
                    )
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Seção de Filtro
                item {
                    val currentState = uiState
                    if (currentState is ExerciseLibraryUiState.Success) {
                        MuscleGroupFilter(
                            selectedGroup = currentState.selectedMuscleGroup,
                            onGroupSelected = viewModel::selectMuscleGroup
                        )
                    }
                }

                // Conteúdo principal (Lista ou Estados)
                when (val state = uiState) {
                    is ExerciseLibraryUiState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier.fillParentMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    CircularProgressIndicator(color = Color.White)
                                    Text(
                                        "Carregando exercícios...",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }
                    }
                    is ExerciseLibraryUiState.Success -> {
                        if (state.filteredExercises.isEmpty()) {
                            item {
                                EmptyState()
                            }
                        } else {
                            items(state.filteredExercises, key = { it.id }) { exercise ->
                                ExerciseCard(
                                    exercise = exercise,
                                    onClick = { onExerciseClick(exercise) }
                                )
                            }
                        }
                    }
                    is ExerciseLibraryUiState.Error -> {
                        item {
                            ErrorState(
                                message = state.message,
                                onRetry = { viewModel.loadExercises() }
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuscleGroupFilter(
    selectedGroup: MuscleGroup,
    onGroupSelected: (MuscleGroup) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Filtrar por Grupo Muscular",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(MuscleGroup.values()) { group ->
                val isSelected = selectedGroup == group
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        val newSelection = if (isSelected) MuscleGroup.ALL else group
                        onGroupSelected(newSelection)
                    },
                    label = {
                        Text(
                            text = "${group.emoji} ${group.displayName}",
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    leadingIcon = if (isSelected) {
                        { Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(FilterChipDefaults.IconSize)) }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.White.copy(alpha = 0.1f),
                        labelColor = Color.White.copy(alpha = 0.8f),
                        selectedContainerColor = accentLightColor,
                        selectedLabelColor = accentColor,
                        selectedLeadingIconColor = accentColor
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = Color.White.copy(alpha = 0.2f),
                        selectedBorderColor = accentColor,
                        borderWidth = 1.dp,
                        selectedBorderWidth = 1.5.dp
                    )
                )
            }
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: Exercise,
    onClick: () -> Unit
) {
    // CORREÇÃO APLICADA AQUI
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            // A borda foi movida para o modifier, que é a forma correta para o ElevatedCard
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White.copy(alpha = 0.05f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        // O parâmetro 'border' foi removido daqui
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagem
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                if (exercise.mediaUrl.isNullOrEmpty()) {
                    Icon(
                        Icons.Default.FitnessCenter,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = accentColor.copy(alpha = 0.6f)
                    )
                } else {
                    AsyncImage(
                        model = exercise.mediaUrl,
                        contentDescription = exercise.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Informações
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = exercise.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White.copy(alpha = 0.7f),
                    lineHeight = 16.sp
                )

                // Tags
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TagChip(
                        text = "${MuscleGroup.fromApiValue(exercise.muscleGroup).emoji} ${MuscleGroup.fromApiValue(exercise.muscleGroup).displayName}",
                        backgroundColor = accentLightColor,
                        textColor = accentColor,
                        borderColor = accentBorderColor
                    )
                    TagChip(
                        text = exercise.equipment,
                        backgroundColor = secondaryLightColor,
                        textColor = secondaryAccentColor,
                        borderColor = secondaryBorderColor
                    )
                }
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}


@Composable
fun TagChip(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    borderColor: Color
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = errorLightColor),
        border = BorderStroke(1.dp, errorColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                Icons.Default.ErrorOutline,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = errorColor
            )
            Text(
                text = "Ops! Algo deu errado",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = errorColor
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = errorColor)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tentar Novamente")
            }
        }
    }
}

@Composable
fun EmptyState() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                Icons.Default.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = "Nenhum exercício encontrado",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Tente selecionar outro grupo muscular ou verifique seus filtros.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}