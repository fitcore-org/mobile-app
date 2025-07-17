package com.example.fitcore.infrastructure.data.remote.adapter

import com.example.fitcore.domain.model.Exercise
import com.example.fitcore.domain.repository.ExerciseRepositoryPort
import com.example.fitcore.infrastructure.data.remote.api.ExerciseApiService
import com.example.fitcore.infrastructure.data.remote.mapper.ExerciseMapper

class ExerciseRepositoryAdapter(
    private val apiService: ExerciseApiService
) : ExerciseRepositoryPort {
    
    override suspend fun getAllExercises(): List<Exercise> {
        return try {
            val exercises = apiService.getAllExercises().map { dto ->
                ExerciseMapper.toDomain(dto)
            }
            exercises
        } catch (e: Exception) {
            // Relançar a exceção para que o ViewModel possa tratar adequadamente
            throw e
        }
    }
    
    override suspend fun getExercisesByMuscleGroup(muscleGroup: String): List<Exercise> {
        return try {
            getAllExercises().filter { 
                it.muscleGroup.equals(muscleGroup, ignoreCase = true) 
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    override suspend fun getExerciseById(id: String): Exercise? {
        return try {
            getAllExercises().find { it.id == id }
        } catch (e: Exception) {
            null
        }
    }
}
