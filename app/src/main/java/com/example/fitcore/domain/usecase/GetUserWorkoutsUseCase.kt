package com.example.fitcore.domain.usecase

import com.example.fitcore.domain.model.Workout
import com.example.fitcore.domain.repository.WorkoutRepositoryPort

class GetUserWorkoutsUseCase(private val workoutRepository: WorkoutRepositoryPort) {
    suspend operator fun invoke(userId: Int): List<Workout> {
        return workoutRepository.getWorkoutsForUser(userId)
    }
}