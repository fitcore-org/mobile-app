package com.example.fitcore.application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitcore.domain.model.Exercise
import com.example.fitcore.domain.model.MuscleGroup
import com.example.fitcore.domain.repository.ExerciseRepositoryPort
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ExerciseLibraryUiState {
    object Loading : ExerciseLibraryUiState()
    data class Success(
        val exercises: List<Exercise>,
        val filteredExercises: List<Exercise>,
        val selectedMuscleGroup: MuscleGroup
    ) : ExerciseLibraryUiState()
    data class Error(val message: String) : ExerciseLibraryUiState()
}

class ExerciseLibraryViewModel(
    private val exerciseRepository: ExerciseRepositoryPort
) : ViewModel() {

    private val _uiState = MutableStateFlow<ExerciseLibraryUiState>(ExerciseLibraryUiState.Loading)
    val uiState: StateFlow<ExerciseLibraryUiState> = _uiState.asStateFlow()

    private var allExercises: List<Exercise> = emptyList()
    private var selectedMuscleGroup: MuscleGroup = MuscleGroup.ALL

    fun loadExercises() {
        viewModelScope.launch {
            _uiState.value = ExerciseLibraryUiState.Loading
            try {
                allExercises = exerciseRepository.getAllExercises()
                updateFilteredExercises()
            } catch (e: Exception) {
                _uiState.value = ExerciseLibraryUiState.Error(
                    message = "Não foi possível carregar os exercícios. Verifique sua conexão."
                )
            }
        }
    }

    fun selectMuscleGroup(muscleGroup: MuscleGroup) {
        selectedMuscleGroup = muscleGroup
        updateFilteredExercises()
    }

    private fun updateFilteredExercises() {
        val filteredExercises = if (selectedMuscleGroup == MuscleGroup.ALL) {
            allExercises
        } else {
            allExercises.filter { 
                MuscleGroup.fromApiValue(it.muscleGroup) == selectedMuscleGroup 
            }
        }

        _uiState.value = ExerciseLibraryUiState.Success(
            exercises = allExercises,
            filteredExercises = filteredExercises,
            selectedMuscleGroup = selectedMuscleGroup
        )
    }
}
