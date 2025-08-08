package com.example.fitcore.infrastructure.data.remote.adapter

import com.example.fitcore.domain.model.PersonalizedWorkout
import com.example.fitcore.domain.repository.PersonalizedWorkoutRepositoryPort
import com.example.fitcore.infrastructure.data.remote.api.RetrofitInstance
import com.example.fitcore.infrastructure.data.remote.mapper.toDomain

class PersonalizedWorkoutRepositoryAdapter : PersonalizedWorkoutRepositoryPort {
    override suspend fun getPersonalizedWorkouts(studentId: String): List<PersonalizedWorkout> {
        return try {
            println("🌐 Buscando treinos personalizados para o estudante: $studentId")
            val workouts = RetrofitInstance.personalizedWorkoutApi.getPersonalizedWorkouts(studentId)
            println("📨 Total de treinos personalizados recebidos: ${workouts.size}")
            workouts.map { it.toDomain() }
        } catch (e: Exception) {
            println("💥 Erro ao buscar treinos personalizados: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
}
