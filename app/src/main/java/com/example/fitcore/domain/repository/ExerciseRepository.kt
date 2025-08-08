package com.example.fitcore.domain.repository

interface ExerciseRepository {
    suspend fun getAllExercises(): List<Any> // TODO: Replace Any with proper Exercise type
}
