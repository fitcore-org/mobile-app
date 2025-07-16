package com.example.fitcore.application.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitcore.domain.model.Workout
import com.example.fitcore.domain.usecase.GetUserWorkoutsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
sealed class WorkoutUiState {
    object Loading : WorkoutUiState(); data class Success(val workouts: List<Workout>) : WorkoutUiState(); data class Error(val message: String) : WorkoutUiState()
}
class WorkoutViewModel(private val getUserWorkoutsUseCase: GetUserWorkoutsUseCase, private val userId: Int) : ViewModel() {
    private val _uiState = MutableStateFlow<WorkoutUiState>(WorkoutUiState.Loading)
    val uiState = _uiState.asStateFlow()
    init { loadWorkouts() }
    private fun loadWorkouts() {
        viewModelScope.launch {
            _uiState.value = WorkoutUiState.Loading
            val workouts = getUserWorkoutsUseCase(userId)
            _uiState.value = if (workouts.isNotEmpty()) { WorkoutUiState.Success(workouts) } else { WorkoutUiState.Error("Não foi possível carregar os treinos.") }
        }
    }
}