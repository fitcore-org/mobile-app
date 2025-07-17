package com.example.fitcore.domain.model

enum class WorkoutSessionState {
    PREPARATION,    // Preparação para o exercício
    EXERCISING,     // Executando o exercício  
    RESTING,        // Descansando entre séries
    NEXT_EXERCISE,  // Transição para próximo exercício
    COMPLETED       // Treino finalizado
}

data class WorkoutSession(
    val workout: EnrichedWorkout,
    val currentExerciseIndex: Int = 0,
    val currentSet: Int = 1,
    val state: WorkoutSessionState = WorkoutSessionState.PREPARATION,
    val timeRemaining: Int = 0, // em segundos
    val completedExercises: Set<String> = emptySet(),
    val startTime: Long = System.currentTimeMillis()
) {
    val currentWorkoutItem: WorkoutItem?
        get() = if (currentExerciseIndex < workout.items.size) {
            workout.items[currentExerciseIndex]
        } else null
    
    val totalExercises: Int
        get() = workout.items.size
    
    val progressPercentage: Float
        get() = when {
            state == WorkoutSessionState.COMPLETED -> 100f
            totalExercises > 0 -> {
                val completedExercises = if (state == WorkoutSessionState.COMPLETED) {
                    totalExercises.toFloat()
                } else {
                    currentExerciseIndex.toFloat()
                }
                (completedExercises / totalExercises.toFloat()) * 100f
            }
            else -> 0f
        }
    
    val isCompleted: Boolean
        get() = state == WorkoutSessionState.COMPLETED
}
