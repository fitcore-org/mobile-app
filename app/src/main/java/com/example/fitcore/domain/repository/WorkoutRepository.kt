package com.example.fitcore.domain.repository

interface WorkoutRepository {
    suspend fun getCurrentTrainingPlan(): Any // TODO: Replace Any with proper return type
    suspend fun getUserWorkouts(): List<Any> // TODO: Replace Any with proper Workout type
}
