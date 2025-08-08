package com.example.fitcore.domain.repository

import com.example.fitcore.domain.model.PublicWorkout

interface PublicWorkoutRepositoryPort {
    suspend fun getPublicWorkouts(): List<PublicWorkout>
    suspend fun getPublicWorkoutById(workoutId: String): PublicWorkout?
}
