package com.example.fitcore.application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitcore.domain.model.PublicWorkout
import com.example.fitcore.domain.model.WorkoutSession
import com.example.fitcore.domain.model.WorkoutSessionState
import com.example.fitcore.domain.model.EnrichedWorkout
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class WorkoutExecutionFromDataUiState {
    object Loading : WorkoutExecutionFromDataUiState()
    data class Success(val session: WorkoutSession) : WorkoutExecutionFromDataUiState()
    data class Error(val message: String) : WorkoutExecutionFromDataUiState()
}

class WorkoutExecutionFromDataViewModel(
    private val workout: PublicWorkout
) : ViewModel() {

    private val _uiState = MutableStateFlow<WorkoutExecutionFromDataUiState>(WorkoutExecutionFromDataUiState.Loading)
    val uiState: StateFlow<WorkoutExecutionFromDataUiState> = _uiState.asStateFlow()

    private var currentSession: WorkoutSession? = null
    private var timerJob: Job? = null

    init {
        initializeWorkout()
    }

    private fun initializeWorkout() {
        viewModelScope.launch {
            try {
                // Convert PublicWorkout to EnrichedWorkout for compatibility
                val enrichedWorkout = EnrichedWorkout(
                    id = workout.id,
                    name = workout.name,
                    description = workout.description,
                    isPublic = workout.isPublic,
                    items = workout.items
                )
                
                val session = WorkoutSession(
                    workout = enrichedWorkout,
                    state = WorkoutSessionState.PREPARATION,
                    timeRemaining = 10 // 10 segundos de preparação inicial
                )
                
                currentSession = session
                _uiState.value = WorkoutExecutionFromDataUiState.Success(session)
                startTimer()
            } catch (e: Exception) {
                _uiState.value = WorkoutExecutionFromDataUiState.Error(
                    e.message ?: "Erro ao inicializar treino"
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
                    _uiState.value = WorkoutExecutionFromDataUiState.Success(newSession)
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
                    _uiState.value = WorkoutExecutionFromDataUiState.Success(newSession)
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
                            _uiState.value = WorkoutExecutionFromDataUiState.Success(newSession)
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
                _uiState.value = WorkoutExecutionFromDataUiState.Success(newSession)
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
                        _uiState.value = WorkoutExecutionFromDataUiState.Success(newSession)
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
                _uiState.value = WorkoutExecutionFromDataUiState.Success(newSession)
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
                _uiState.value = WorkoutExecutionFromDataUiState.Success(newSession)
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
            _uiState.value = WorkoutExecutionFromDataUiState.Success(newSession)
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
