package com.example.fitcore.domain.repository

interface EnrichedWorkoutRepository {
    suspend fun getChestWorkout(): Any // TODO: Replace Any with proper return type
}
