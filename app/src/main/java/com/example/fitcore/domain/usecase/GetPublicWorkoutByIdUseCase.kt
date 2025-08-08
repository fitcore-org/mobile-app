package com.example.fitcore.domain.usecase

import com.example.fitcore.domain.model.PublicWorkout
import com.example.fitcore.domain.repository.PublicWorkoutRepositoryPort

class GetPublicWorkoutByIdUseCase(private val repository: PublicWorkoutRepositoryPort) {
    suspend operator fun invoke(workoutId: String): PublicWorkout? {
        return repository.getPublicWorkoutById(workoutId)
    }
}
