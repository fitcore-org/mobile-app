package com.example.fitcore.infrastructure.data.remote.api

import com.example.fitcore.infrastructure.data.remote.dto.PublicWorkoutDto
import retrofit2.http.GET
import retrofit2.http.Path

interface WorkoutApiService {
    @GET("api/v1/workouts/public")
    suspend fun getPublicWorkouts(): List<PublicWorkoutDto>
    
    @GET("api/v1/workouts/public/{workoutId}")
    suspend fun getPublicWorkoutById(@Path("workoutId") workoutId: String): PublicWorkoutDto
}
