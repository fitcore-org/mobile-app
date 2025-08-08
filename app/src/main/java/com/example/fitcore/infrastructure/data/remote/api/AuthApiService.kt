package com.example.fitcore.infrastructure.data.remote.api

import com.example.fitcore.infrastructure.data.remote.dto.LoginRequestDto
import com.example.fitcore.infrastructure.data.remote.dto.LoginResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<LoginResponseDto>
}
