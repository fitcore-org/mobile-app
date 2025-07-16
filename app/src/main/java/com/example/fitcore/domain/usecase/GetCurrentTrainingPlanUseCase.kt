package com.example.fitcore.domain.usecase

import com.example.fitcore.domain.model.TrainingPlan
import com.example.fitcore.domain.repository.WorkoutRepositoryPort

class GetCurrentTrainingPlanUseCase(private val workoutRepository: WorkoutRepositoryPort) {
    suspend operator fun invoke(): TrainingPlan? {
        return workoutRepository.getCurrentTrainingPlan()
    }
}