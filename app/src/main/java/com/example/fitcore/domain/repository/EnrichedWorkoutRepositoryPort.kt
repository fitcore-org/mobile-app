package com.example.fitcore.domain.repository

import com.example.fitcore.domain.model.EnrichedWorkout

interface EnrichedWorkoutRepositoryPort {
    suspend fun getEnrichedWorkouts(): List<EnrichedWorkout>
    suspend fun getChestWorkout(): EnrichedWorkout?
}
