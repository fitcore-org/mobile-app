package com.example.fitcore.domain.usecase

import com.example.fitcore.domain.model.PublicWorkout
import com.example.fitcore.domain.repository.PublicWorkoutRepositoryPort

class GetPublicWorkoutsUseCase(private val repository: PublicWorkoutRepositoryPort) {
    suspend operator fun invoke(): List<PublicWorkout> {
        return repository.getPublicWorkouts()
    }
}
