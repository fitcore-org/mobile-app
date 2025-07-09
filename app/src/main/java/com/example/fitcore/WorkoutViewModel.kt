package com.example.fitcore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
sealed class WorkoutUiState {
    object Loading : WorkoutUiState(); data class Success(val workouts: List<Post>) : WorkoutUiState(); data class Error(val message: String) : WorkoutUiState()
}
class WorkoutViewModel(private val workoutRepository: WorkoutRepository, private val userId: Int) : ViewModel() {
    private val _uiState = MutableStateFlow<WorkoutUiState>(WorkoutUiState.Loading)
    val uiState = _uiState.asStateFlow()
    init { loadWorkouts() }
    private fun loadWorkouts() {
        viewModelScope.launch {
            _uiState.value = WorkoutUiState.Loading
            val workouts = workoutRepository.getWorkoutsForUser(userId)
            _uiState.value = if (workouts.isNotEmpty()) { WorkoutUiState.Success(workouts) } else { WorkoutUiState.Error("Não foi possível carregar os treinos.") }
        }
    }
}