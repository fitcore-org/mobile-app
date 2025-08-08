package com.example.fitcore.infrastructure.data.remote.api

import com.example.fitcore.infrastructure.data.remote.dto.ExerciseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ExerciseApiService {
    @GET("api/v1/exercises")
    suspend fun getAllExercises(): List<ExerciseDto>
    
    @GET("api/v1/exercises/{exerciseId}")
    suspend fun getExerciseById(@Path("exerciseId") exerciseId: String): ExerciseDto
}
