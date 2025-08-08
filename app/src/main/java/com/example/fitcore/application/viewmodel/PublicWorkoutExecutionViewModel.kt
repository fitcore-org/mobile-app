package com.example.fitcore.application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitcore.domain.model.PublicWorkout
import com.example.fitcore.domain.model.WorkoutSession
import com.example.fitcore.domain.model.WorkoutSessionState
import com.example.fitcore.domain.model.EnrichedWorkout
import com.example.fitcore.domain.usecase.GetPublicWorkoutByIdUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class PublicWorkoutExecutionUiState {
    object Loading : PublicWorkoutExecutionUiState()
    data class Success(val session: WorkoutSession) : PublicWorkoutExecutionUiState()
    data class Error(val message: String) : PublicWorkoutExecutionUiState()
}

class PublicWorkoutExecutionViewModel(
    private val getPublicWorkoutByIdUseCase: GetPublicWorkoutByIdUseCase,
    private val workoutId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<PublicWorkoutExecutionUiState>(PublicWorkoutExecutionUiState.Loading)
    val uiState: StateFlow<PublicWorkoutExecutionUiState> = _uiState.asStateFlow()

    private var currentSession: WorkoutSession? = null
    private var timerJob: Job? = null

    init {
        loadWorkout()
    }

    private fun loadWorkout() {
        viewModelScope.launch {
            _uiState.value = PublicWorkoutExecutionUiState.Loading
            try {
                val publicWorkout = getPublicWorkoutByIdUseCase(workoutId)
                if (publicWorkout != null) {
                    // Convert PublicWorkout to EnrichedWorkout for compatibility
                    val enrichedWorkout = EnrichedWorkout(
                        id = publicWorkout.id,
                        name = publicWorkout.name,
                        description = publicWorkout.description,
                        isPublic = publicWorkout.isPublic,
                        items = publicWorkout.items
                    )
                    
                    val session = WorkoutSession(
                        workout = enrichedWorkout,
                        state = WorkoutSessionState.PREPARATION,
                        timeRemaining = 10 // 10 segundos de preparação inicial
                    )
                    
                    currentSession = session
                    _uiState.value = PublicWorkoutExecutionUiState.Success(session)
                    startTimer()
                } else {
                    _uiState.value = PublicWorkoutExecutionUiState.Error("Treino não encontrado")
                }
            } catch (e: Exception) {
                _uiState.value = PublicWorkoutExecutionUiState.Error(
                    e.message ?: "Erro ao carregar treino"
                )
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            currentSession?.let { session ->
                var timeLeft = session.timeRemaining
                while (timeLeft > 0) {
                    delay(1000)
                    timeLeft--
                    val newSession = session.copy(timeRemaining = timeLeft)
                    currentSession = newSession
                    _uiState.value = PublicWorkoutExecutionUiState.Success(newSession)
                }
                onTimerFinished()
            }
        }
    }

    private fun onTimerFinished() {
        currentSession?.let { session ->
            when (session.state) {
                WorkoutSessionState.PREPARATION -> {
                    val newSession = session.copy(
                        state = WorkoutSessionState.EXERCISING,
                        timeRemaining = 0
                    )
                    currentSession = newSession
                    _uiState.value = PublicWorkoutExecutionUiState.Success(newSession)
                }

                WorkoutSessionState.RESTING -> {
                    val currentItem = session.currentWorkoutItem
                    if (currentItem != null) {
                        val maxSets = currentItem.sets.split("-").firstOrNull()?.toIntOrNull() ?: 1

                        if (session.currentSet < maxSets) {
                            val newSession = session.copy(
                                state = WorkoutSessionState.PREPARATION,
                                currentSet = session.currentSet + 1,
                                timeRemaining = 5
                            )
                            currentSession = newSession
                            _uiState.value = PublicWorkoutExecutionUiState.Success(newSession)
                            startTimer()
                        } else {
                            nextExercise()
                        }
                    }
                }

                else -> {}
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
                _uiState.value = PublicWorkoutExecutionUiState.Success(newSession)
            }
        }
    }

    fun finishSet() {
        currentSession?.let { session ->
            if (session.state == WorkoutSessionState.EXERCISING) {
                val currentItem = session.currentWorkoutItem
                if (currentItem != null) {
                    val maxSets = currentItem.sets.split("-").firstOrNull()?.toIntOrNull() ?: 1

                    if (session.currentSet < maxSets) {
                        val newSession = session.copy(
                            state = WorkoutSessionState.RESTING,
                            timeRemaining = currentItem.restSeconds
                        )
                        currentSession = newSession
                        _uiState.value = PublicWorkoutExecutionUiState.Success(newSession)
                        startTimer()
                    } else {
                        nextExercise()
                    }
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
                finishWorkout()
            } else {
                val newSession = session.copy(
                    currentExerciseIndex = session.currentExerciseIndex + 1,
                    currentSet = 1,
                    state = WorkoutSessionState.PREPARATION,
                    timeRemaining = 10,
                    completedExercises = session.completedExercises + (session.currentWorkoutItem?.id ?: "")
                )
                currentSession = newSession
                _uiState.value = PublicWorkoutExecutionUiState.Success(newSession)
                startTimer()
            }
        }
    }

    fun previousExercise() {
        currentSession?.let { session ->
            if (session.currentExerciseIndex > 0) {
                val newSession = session.copy(
                    currentExerciseIndex = session.currentExerciseIndex - 1,
                    currentSet = 1,
                    state = WorkoutSessionState.PREPARATION,
                    timeRemaining = 10
                )
                currentSession = newSession
                _uiState.value = PublicWorkoutExecutionUiState.Success(newSession)
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
            _uiState.value = PublicWorkoutExecutionUiState.Success(newSession)
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
