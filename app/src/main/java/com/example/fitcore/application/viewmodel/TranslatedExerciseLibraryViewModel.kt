package com.example.fitcore.application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitcore.domain.model.Exercise
import com.example.fitcore.domain.model.MuscleGroup
import com.example.fitcore.domain.model.TranslatedExercise
import com.example.fitcore.domain.repository.ExerciseRepositoryPort
import com.example.fitcore.infrastructure.service.OptimizedTranslationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class TranslatedExerciseLibraryUiState {
    object Loading : TranslatedExerciseLibraryUiState()
    data class Success(
        val exercises: List<TranslatedExercise>,
        val filteredExercises: List<TranslatedExercise>,
        val selectedMuscleGroup: MuscleGroup,
        val isTranslationAvailable: Boolean = true
    ) : TranslatedExerciseLibraryUiState()
    data class Error(val message: String) : TranslatedExerciseLibraryUiState()
}

class TranslatedExerciseLibraryViewModel(
    private val exerciseRepository: ExerciseRepositoryPort,
    private val translationService: OptimizedTranslationService = OptimizedTranslationService.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow<TranslatedExerciseLibraryUiState>(
        TranslatedExerciseLibraryUiState.Loading
    )
    val uiState: StateFlow<TranslatedExerciseLibraryUiState> = _uiState.asStateFlow()

    private var allExercises: List<TranslatedExercise> = emptyList()
    private var selectedMuscleGroup: MuscleGroup = MuscleGroup.ALL

    fun loadExercises() {
        viewModelScope.launch {
            _uiState.value = TranslatedExerciseLibraryUiState.Loading
            
            try {
                // 1. Carrega exercícios originais
                val originalExercises = exerciseRepository.getAllExercises()
                
                // 2. Traduz instantaneamente usando dicionário local
                val translatedExercises = originalExercises.map { exercise ->
                    TranslatedExercise(
                        id = exercise.id,
                        originalName = exercise.name,
                        translatedName = translationService.translateExerciseName(exercise.name),
                        originalDescription = exercise.description,
                        translatedDescription = translationService.translateDescription(exercise.description),
                        muscleGroup = exercise.muscleGroup,
                        equipment = exercise.equipment,
                        originalEquipment = exercise.equipment,
                        translatedEquipment = translationService.translateEquipment(exercise.equipment),
                        mediaUrl = exercise.mediaUrl,
                        mediaUrl2 = exercise.mediaUrl2,
                        isTranslated = true // Sempre traduzido com o novo sistema
                    )
                }
                
                allExercises = translatedExercises
                updateFilteredExercises()
                
            } catch (e: Exception) {
                _uiState.value = TranslatedExerciseLibraryUiState.Error(
                    message = "Erro ao carregar exercícios: ${e.message}"
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

        _uiState.value = TranslatedExerciseLibraryUiState.Success(
            exercises = allExercises,
            filteredExercises = filteredExercises,
            selectedMuscleGroup = selectedMuscleGroup,
            isTranslationAvailable = true // Sempre disponível
        )
    }

    // Não precisamos mais de retry - tradução é instantânea
    fun retryTranslation() {
        loadExercises()
    }
}
