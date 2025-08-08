package com.example.fitcore.infrastructure.data.remote.adapter
import com.example.fitcore.domain.model.TrainingPlan
import com.example.fitcore.domain.model.Workout
import com.example.fitcore.domain.repository.WorkoutRepositoryPort
import com.example.fitcore.infrastructure.data.remote.api.RetrofitInstance
import com.example.fitcore.infrastructure.data.remote.mapper.toDomain

class WorkoutRepositoryAdapter : WorkoutRepositoryPort {
    override suspend fun getWorkoutsForUser(userId: String): List<Workout> {
        return try {
            // Busca treinos personalizados do usuário
            val personalizedWorkouts = RetrofitInstance.api.getPostsForUser(userId).map { it.toDomain() }
            // Busca treinos públicos
            val publicWorkouts = RetrofitInstance.api.getPublicWorkouts().map { it.toDomain() }
            // Combina os dois tipos de treinos
            personalizedWorkouts + publicWorkouts
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getCurrentTrainingPlan(): TrainingPlan? {
        return try {
            val workoutsDto = RetrofitInstance.api.getPostsForUser("default").take(3)
            if (workoutsDto.isNotEmpty()) {
                TrainingPlan(id = 1, name = "Plano de Hipertrofia Inicial", workouts = workoutsDto.map { it.toDomain() })
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}