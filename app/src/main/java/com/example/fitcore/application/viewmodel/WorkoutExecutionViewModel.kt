package com.example.fitcore.application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitcore.domain.model.EnrichedWorkout
import com.example.fitcore.domain.model.WorkoutSession
import com.example.fitcore.domain.model.WorkoutSessionState
import com.example.fitcore.domain.usecase.GetChestWorkoutUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class WorkoutExecutionUiState {
    object Loading : WorkoutExecutionUiState()
    data class Success(val session: WorkoutSession) : WorkoutExecutionUiState()
    data class Error(val message: String) : WorkoutExecutionUiState()
}

class WorkoutExecutionViewModel(
    private val getChestWorkoutUseCase: GetChestWorkoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<WorkoutExecutionUiState>(WorkoutExecutionUiState.Loading)
    val uiState: StateFlow<WorkoutExecutionUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var currentSession: WorkoutSession? = null

    init {
        loadChestWorkout()
    }
    
    private fun loadTestWorkout() {
        println("🧪 Carregando treino simulado para teste de imagens...")
        viewModelScope.launch {
            try {
                // Criar dados simulados
                val testWorkout = createTestWorkout()
                currentSession = WorkoutSession(
                    workout = testWorkout,
                    state = WorkoutSessionState.PREPARATION,
                    timeRemaining = 10
                )
                _uiState.value = WorkoutExecutionUiState.Success(currentSession!!)
                startTimer()
            } catch (e: Exception) {
                println("💥 Erro ao criar treino simulado: ${e.message}")
                _uiState.value = WorkoutExecutionUiState.Error("Erro ao criar treino simulado: ${e.message}")
            }
        }
    }
    
    private fun createTestWorkout(): EnrichedWorkout {
        val testExercise1 = com.example.fitcore.domain.model.Exercise(
            id = "test1",
            name = "Supino Reto com Barra",
            description = "Exercício fundamental para desenvolvimento do peitoral maior.",
            muscleGroup = "Peito",
            equipment = "Barra",
            mediaUrl = "https://example.com/supino1.jpg",
            mediaUrl2 = "https://example.com/supino2.jpg"
        )
        
        val testExercise2 = com.example.fitcore.domain.model.Exercise(
            id = "test2", 
            name = "Supino Inclinado com Halteres",
            description = "Foca na porção superior do peitoral.",
            muscleGroup = "Peito superior",
            equipment = "Halteres",
            mediaUrl = "https://example.com/inclinado1.jpg",
            mediaUrl2 = "https://example.com/inclinado2.jpg"
        )
        
        val workoutItems = listOf(
            com.example.fitcore.domain.model.WorkoutItem(
                id = "item1",
                exercise = testExercise1,
                sets = "4",
                reps = "8-10",
                restSeconds = 120,
                observation = "Foque na explosão na subida.",
                order = 1
            ),
            com.example.fitcore.domain.model.WorkoutItem(
                id = "item2",
                exercise = testExercise2, 
                sets = "3",
                reps = "10-12",
                restSeconds = 90,
                observation = "Controle o movimento na descida.",
                order = 2
            )
        )
        
        return EnrichedWorkout(
            id = "test-workout",
            name = "Treino de Peito - Teste",
            description = "Treino simulado para teste de imagens",
            isPublic = false,
            items = workoutItems
        )
    }

    private fun loadChestWorkout() {
        viewModelScope.launch {
            try {
                println("🔄 Iniciando busca por treino de peito...")
                val workout = getChestWorkoutUseCase()
                println("📋 Resultado do use case: $workout")
                
                if (workout != null) {
                    println("✅ Treino encontrado: ${workout.name}")
                    currentSession = WorkoutSession(
                        workout = workout,
                        state = WorkoutSessionState.PREPARATION,
                        timeRemaining = 10 // 10 segundos de preparação
                    )
                    _uiState.value = WorkoutExecutionUiState.Success(currentSession!!)
                    startTimer()
                } else {
                    println("❌ Treino de peito não encontrado")
                    _uiState.value = WorkoutExecutionUiState.Error("Treino de peito não encontrado")
                }
            } catch (e: Exception) {
                println("💥 Erro ao carregar treino: ${e.message}")
                e.printStackTrace()
                _uiState.value = WorkoutExecutionUiState.Error("Erro ao carregar treino: ${e.message}")
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (currentSession?.timeRemaining ?: 0 > 0) {
                delay(1000)
                currentSession?.let { session ->
                    val newSession = session.copy(timeRemaining = session.timeRemaining - 1)
                    currentSession = newSession
                    _uiState.value = WorkoutExecutionUiState.Success(newSession)
                }
            }
            // Timer acabou
            onTimerFinished()
        }
    }

    private fun onTimerFinished() {
        currentSession?.let { session ->
            when (session.state) {
                WorkoutSessionState.PREPARATION -> {
                    // Preparação acabou, começar exercício
                    val newSession = session.copy(
                        state = WorkoutSessionState.EXERCISING,
                        timeRemaining = 0 // exercício não tem timer automático
                    )
                    currentSession = newSession
                    _uiState.value = WorkoutExecutionUiState.Success(newSession)
                }
                
                WorkoutSessionState.RESTING -> {
                    // Descanso acabou
                    val currentItem = session.currentWorkoutItem
                    if (currentItem != null) {
                        val maxSets = currentItem.sets.split("-").firstOrNull()?.toIntOrNull() ?: 1
                        
                        if (session.currentSet < maxSets) {
                            // Próxima série do mesmo exercício
                            val newSession = session.copy(
                                state = WorkoutSessionState.PREPARATION,
                                currentSet = session.currentSet + 1,
                                timeRemaining = 5 // 5 segundos de preparação entre séries
                            )
                            currentSession = newSession
                            _uiState.value = WorkoutExecutionUiState.Success(newSession)
                            startTimer()
                        } else {
                            // Próximo exercício
                            nextExercise()
                        }
                    }
                }
                
                else -> { /* Não faz nada */ }
            }
        }
    }

    fun startExercise() {
        currentSession?.let { session ->
            if (session.state == WorkoutSessionState.PREPARATION) {
                timerJob?.cancel()
                val newSession = session.copy(
                    state = WorkoutSessionState.EXERCISING,
                    timeRemaining = 0
                )
                currentSession = newSession
                _uiState.value = WorkoutExecutionUiState.Success(newSession)
            }
        }
    }

    fun finishSet() {
        currentSession?.let { session ->
            if (session.state == WorkoutSessionState.EXERCISING) {
                val currentItem = session.currentWorkoutItem
                if (currentItem != null) {
                    val newSession = session.copy(
                        state = WorkoutSessionState.RESTING,
                        timeRemaining = currentItem.restSeconds
                    )
                    currentSession = newSession
                    _uiState.value = WorkoutExecutionUiState.Success(newSession)
                    startTimer()
                }
            }
        }
    }

    fun skipRest() {
        currentSession?.let { session ->
            if (session.state == WorkoutSessionState.RESTING) {
                timerJob?.cancel()
                onTimerFinished()
            }
        }
    }

    fun nextExercise() {
        currentSession?.let { session ->
            if (session.currentExerciseIndex + 1 >= session.totalExercises) {
                // Treino finalizado
                finishWorkout()
            } else {
                // Próximo exercício
                val newSession = session.copy(
                    currentExerciseIndex = session.currentExerciseIndex + 1,
                    currentSet = 1,
                    state = WorkoutSessionState.PREPARATION,
                    timeRemaining = 10, // 10 segundos de preparação
                    completedExercises = session.completedExercises + (session.currentWorkoutItem?.id ?: "")
                )
                currentSession = newSession
                _uiState.value = WorkoutExecutionUiState.Success(newSession)
                startTimer()
            }
        }
    }

    fun previousExercise() {
        currentSession?.let { session ->
            if (session.currentExerciseIndex > 0) {
                timerJob?.cancel()
                val newSession = session.copy(
                    currentExerciseIndex = session.currentExerciseIndex - 1,
                    currentSet = 1,
                    state = WorkoutSessionState.PREPARATION,
                    timeRemaining = 10
                )
                currentSession = newSession
                _uiState.value = WorkoutExecutionUiState.Success(newSession)
                startTimer()
            }
        }
    }

    private fun finishWorkout() {
        timerJob?.cancel()
        currentSession?.let { session ->
            val newSession = session.copy(
                state = WorkoutSessionState.COMPLETED,
                timeRemaining = 0
            )
            currentSession = newSession
            _uiState.value = WorkoutExecutionUiState.Success(newSession)
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
    }

    fun resumeTimer() {
        currentSession?.let { session ->
            if (session.timeRemaining > 0) {
                startTimer()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
