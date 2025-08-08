package com.example.fitcore.domain.repository

import com.example.fitcore.domain.model.TrainingPlan
import com.example.fitcore.domain.model.Workout

interface WorkoutRepositoryPort {
    suspend fun getWorkoutsForUser(userId: String): List<Workout>
    suspend fun getCurrentTrainingPlan(): TrainingPlan?
}