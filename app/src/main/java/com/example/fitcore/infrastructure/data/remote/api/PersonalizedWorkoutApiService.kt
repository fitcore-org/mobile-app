package com.example.fitcore.infrastructure.data.remote.api

import com.example.fitcore.infrastructure.data.remote.dto.PersonalizedWorkoutDto
import retrofit2.http.GET
import retrofit2.http.Path

interface PersonalizedWorkoutApiService {
    @GET("api/v1/workouts/student/{studentId}")
    suspend fun getPersonalizedWorkouts(@Path("studentId") studentId: String): List<PersonalizedWorkoutDto>
}
