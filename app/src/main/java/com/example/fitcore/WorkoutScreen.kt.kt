package com.example.fitcore.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.fitcore.Post
import com.example.fitcore.WorkoutUiState
import com.example.fitcore.WorkoutViewModel

@Composable
fun WorkoutScreen(viewModel: WorkoutViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is WorkoutUiState.Loading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            is WorkoutUiState.Success -> WorkoutList(workouts = state.workouts)
            is WorkoutUiState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun WorkoutList(workouts: List<Post>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(workouts) { workout ->
            WorkoutCard(workout = workout)
        }
    }
}

@Composable
fun WorkoutCard(workout: Post) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(workout.title.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Grupo Muscular: Peito", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(workout.body.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.bodyMedium, maxLines = 3, overflow = TextOverflow.Ellipsis)
        }
    }
}