package com.example.fitcore.infrastructure.data.remote.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // Usando 10.0.2.2 para conectar ao localhost do host via emulador Android
    private const val BASE_URL = "http://10.0.2.2:8080/"
    
    // URL específica para o serviço de exercícios e treinos na porta 8082
    private const val EXERCISE_SERVICE_URL = "http://10.0.2.2:8082/"

    private const val STUDENT_SERVICE_URL = "http://10.0.2.2:8081/"

    val personalizedWorkoutApi: PersonalizedWorkoutApiService by lazy {
        Retrofit.Builder()
            .baseUrl(EXERCISE_SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PersonalizedWorkoutApiService::class.java)
    }

    val studentApi: StudentApi by lazy {
        Retrofit.Builder()
            .baseUrl(STUDENT_SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StudentApi::class.java)
    }

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    
    val authApi: AuthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }
    
    val exerciseApi: ExerciseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(EXERCISE_SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExerciseApiService::class.java)
    }
    
    val workoutApi: WorkoutApiService by lazy {
        Retrofit.Builder()
            .baseUrl(EXERCISE_SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WorkoutApiService::class.java)
    }
}