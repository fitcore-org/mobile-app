package com.example.fitcore.infrastructure.data.remote.api

import com.example.fitcore.infrastructure.data.remote.dto.ExerciseDto
import retrofit2.http.GET

interface ExerciseApiService {
    @GET("api/v1/exercises")
    suspend fun getAllExercises(): List<ExerciseDto>
}
