package com.example.fitcore.infrastructure.data.remote.api
import com.example.fitcore.infrastructure.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
interface ApiService {
    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") id: String): UserDto

    @GET("api/v1/workouts/student/{userId}")
    suspend fun getPostsForUser(@Path("userId") userId: String): List<WorkoutDto>

    @GET("api/v1/workouts/public")
    suspend fun getPublicWorkouts(): List<WorkoutDto>

    @GET("student/me/training-plan/current")
    suspend fun getCurrentTrainingPlan(): Response<TrainingPlanDto>

    @GET("api/v1/workouts/public/enriched")
    suspend fun getEnrichedWorkouts(): List<EnrichedWorkoutDto>

    @GET("workouts/{id}")
    suspend fun getWorkoutDetails(@Path("id") id: String): WorkoutDto
}