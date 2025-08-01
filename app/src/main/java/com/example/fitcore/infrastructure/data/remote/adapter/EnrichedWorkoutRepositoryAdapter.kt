package com.example.fitcore.infrastructure.data.remote.adapter

import com.example.fitcore.domain.model.EnrichedWorkout
import com.example.fitcore.domain.repository.EnrichedWorkoutRepositoryPort
import com.example.fitcore.infrastructure.data.remote.api.RetrofitInstance
import com.example.fitcore.infrastructure.data.remote.mapper.toDomain

class EnrichedWorkoutRepositoryAdapter : EnrichedWorkoutRepositoryPort {
    override suspend fun getEnrichedWorkouts(): List<EnrichedWorkout> {
        return try {
            println("🌐 Fazendo chamada para API: ${RetrofitInstance.api}")
            val response = RetrofitInstance.api.getEnrichedWorkouts()
            println("📨 Resposta da API recebida: ${response.size} treinos")
            response.forEach { workout ->
                println("📋 Treino: ${workout.name}")
            }
            response.map { it.toDomain() }
        } catch (e: Exception) {
            println("💥 Erro na chamada da API: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getChestWorkout(): EnrichedWorkout? {
        return try {
            println("🔍 Buscando treino de peito...")
            val workouts = RetrofitInstance.api.getEnrichedWorkouts()
            println("📨 Total de treinos recebidos: ${workouts.size}")
            
            workouts.forEach { workout ->
                println("📋 Treino disponível: '${workout.name}'")
            }
            
            val chestWorkout = workouts.find { 
                val nameContainsPeito = it.name.contains("Peito", ignoreCase = true)
                val nameContainsChest = it.name.contains("Chest", ignoreCase = true)
                println("🔍 Verificando '${it.name}': peito=$nameContainsPeito, chest=$nameContainsChest")
                nameContainsPeito || nameContainsChest
            }
            
            if (chestWorkout != null) {
                println("✅ Treino de peito encontrado: ${chestWorkout.name}")
                chestWorkout.toDomain()
            } else {
                println("❌ Nenhum treino de peito encontrado")
                null
            }
        } catch (e: Exception) {
            println("💥 Erro ao buscar treino de peito: ${e.message}")
            e.printStackTrace()
            null
        }
    }
}
