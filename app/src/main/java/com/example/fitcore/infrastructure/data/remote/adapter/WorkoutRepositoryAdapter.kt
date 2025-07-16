package com.example.fitcore.infrastructure.data.remote.adapter
import com.example.fitcore.domain.model.TrainingPlan
import com.example.fitcore.domain.model.Workout
import com.example.fitcore.domain.repository.WorkoutRepositoryPort
import com.example.fitcore.infrastructure.data.remote.api.RetrofitInstance
import com.example.fitcore.infrastructure.data.remote.mapper.toDomain

class WorkoutRepositoryAdapter : WorkoutRepositoryPort {
    override suspend fun getWorkoutsForUser(userId: Int): List<Workout> {
        return try {
            RetrofitInstance.api.getPostsForUser(userId).map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getCurrentTrainingPlan(): TrainingPlan? {
        return try {
            val workoutsDto = RetrofitInstance.api.getPostsForUser(1).take(3)
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