package com.example.fitcore.domain.repository

import com.example.fitcore.domain.model.Exercise

interface ExerciseRepositoryPort {
    suspend fun getAllExercises(): List<Exercise>
    suspend fun getExercisesByMuscleGroup(muscleGroup: String): List<Exercise>
    suspend fun getExerciseById(id: String): Exercise?
}
