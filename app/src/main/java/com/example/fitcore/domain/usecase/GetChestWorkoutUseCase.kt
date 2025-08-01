package com.example.fitcore.domain.usecase

import com.example.fitcore.domain.model.EnrichedWorkout
import com.example.fitcore.domain.repository.EnrichedWorkoutRepositoryPort

class GetChestWorkoutUseCase(private val repository: EnrichedWorkoutRepositoryPort) {
    suspend operator fun invoke(): EnrichedWorkout? {
        return repository.getChestWorkout()
    }
}
