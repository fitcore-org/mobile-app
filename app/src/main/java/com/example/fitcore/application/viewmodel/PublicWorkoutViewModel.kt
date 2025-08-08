package com.example.fitcore.application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitcore.domain.model.PublicWorkout
import com.example.fitcore.domain.usecase.GetPublicWorkoutsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class PublicWorkoutUiState {
    object Loading : PublicWorkoutUiState()
    data class Success(val workouts: List<PublicWorkout>) : PublicWorkoutUiState()
    data class Error(val message: String) : PublicWorkoutUiState()
}

class PublicWorkoutViewModel(
    private val getPublicWorkoutsUseCase: GetPublicWorkoutsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PublicWorkoutUiState>(PublicWorkoutUiState.Loading)
    val uiState: StateFlow<PublicWorkoutUiState> = _uiState.asStateFlow()

    private var _workouts = listOf<PublicWorkout>()
    val workouts: List<PublicWorkout> get() = _workouts

    init {
        loadPublicWorkouts()
    }

    private fun loadPublicWorkouts() {
        viewModelScope.launch {
            _uiState.value = PublicWorkoutUiState.Loading
            try {
                val workouts = getPublicWorkoutsUseCase()
                _workouts = workouts
                _uiState.value = PublicWorkoutUiState.Success(workouts)
            } catch (e: Exception) {
                _uiState.value = PublicWorkoutUiState.Error(
                    e.message ?: "Erro ao carregar treinos"
                )
            }
        }
    }

    fun getWorkoutById(workoutId: String): PublicWorkout? {
        return _workouts.find { it.id == workoutId }
    }

    fun retry() {
        loadPublicWorkouts()
    }
}
