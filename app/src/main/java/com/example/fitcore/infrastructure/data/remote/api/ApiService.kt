package com.example.fitcore.infrastructure.data.remote.api
import com.example.fitcore.infrastructure.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<LoginResponseDto>

    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") id: Int): UserDto

    @GET("posts")
    suspend fun getPostsForUser(@Query("userId") userId: Int): List<WorkoutDto>

    @GET("student/me/training-plan/current")
    suspend fun getCurrentTrainingPlan(): Response<TrainingPlanDto>

    @GET("api/v1/workouts/public/enriched")
    suspend fun getEnrichedWorkouts(): List<EnrichedWorkoutDto>

    @GET("workouts/{id}")
    suspend fun getWorkoutDetails(@Path("id") id: Int): WorkoutDto
}