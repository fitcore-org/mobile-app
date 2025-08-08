package com.example.fitcore.application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitcore.domain.model.Workout
import com.example.fitcore.domain.model.PersonalizedWorkout
import com.example.fitcore.domain.usecase.GetUserWorkoutsUseCase
import com.example.fitcore.domain.usecase.GetPersonalizedWorkoutsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class WorkoutUiState {
    object Loading : WorkoutUiState()
    data class Success(
        val publicWorkouts: List<Workout>,
        val personalizedWorkouts: List<PersonalizedWorkout>
    ) : WorkoutUiState()
    data class Error(val message: String) : WorkoutUiState()
}

class WorkoutViewModel(
    private val getUserWorkoutsUseCase: GetUserWorkoutsUseCase,
    private val getPersonalizedWorkoutsUseCase: GetPersonalizedWorkoutsUseCase,
    private val userId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow<WorkoutUiState>(WorkoutUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadWorkouts()
    }

    private fun loadWorkouts() {
        viewModelScope.launch {
            _uiState.value = WorkoutUiState.Loading
            try {
                val personalizedWorkouts = getPersonalizedWorkoutsUseCase(userId)
                val publicWorkouts = getUserWorkoutsUseCase(userId)
                
                if (publicWorkouts.isNotEmpty() || personalizedWorkouts.isNotEmpty()) {
                    _uiState.value = WorkoutUiState.Success(
                        publicWorkouts = publicWorkouts,
                        personalizedWorkouts = personalizedWorkouts
                    )
                } else {
                    _uiState.value = WorkoutUiState.Error("Não foi possível carregar os treinos.")
                }
            } catch (e: Exception) {
                _uiState.value = WorkoutUiState.Error("Erro ao carregar os treinos: ${e.message}")
            }
        }
    }
}