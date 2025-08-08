package com.example.fitcore.infrastructure.data.remote.adapter

import com.example.fitcore.domain.model.PublicWorkout
import com.example.fitcore.domain.repository.PublicWorkoutRepositoryPort
import com.example.fitcore.infrastructure.data.remote.api.RetrofitInstance
import com.example.fitcore.infrastructure.data.remote.mapper.toDomain

class PublicWorkoutRepositoryAdapter : PublicWorkoutRepositoryPort {
    
    private val workoutApi = RetrofitInstance.workoutApi
    
    override suspend fun getPublicWorkouts(): List<PublicWorkout> {
        return try {
            println("🔍 Buscando treinos públicos...")
            val workouts = workoutApi.getPublicWorkouts()
            println("📨 Total de treinos recebidos: ${workouts.size}")
            workouts.forEach { workout ->
                println("📋 Treino disponível: '${workout.name}' (ID: ${workout.id})")
            }
            workouts.map { it.toDomain() }
        } catch (e: Exception) {
            println("💥 Erro ao buscar treinos públicos: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
    
    override suspend fun getPublicWorkoutById(workoutId: String): PublicWorkout? {
        return try {
            println("🔍 Buscando treino específico com ID: $workoutId")
            val workout = workoutApi.getPublicWorkoutById(workoutId)
            println("✅ Treino encontrado: ${workout.name}")
            workout.toDomain()
        } catch (e: Exception) {
            println("💥 Erro ao buscar treino por ID '$workoutId': ${e.message}")
            e.printStackTrace()
            null
        }
    }
}
