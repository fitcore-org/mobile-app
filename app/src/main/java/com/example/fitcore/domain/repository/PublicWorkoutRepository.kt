package com.example.fitcore.domain.repository

interface PublicWorkoutRepository {
    suspend fun getPublicWorkouts(): List<Any> // TODO: Replace Any with proper Workout type
    suspend fun getWorkoutById(id: String): Any // TODO: Replace Any with proper Workout type
}
