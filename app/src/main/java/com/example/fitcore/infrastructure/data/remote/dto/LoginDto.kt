package com.example.fitcore.infrastructure.data.remote.dto
data class LoginRequestDto(val email: String, val password: String)
data class LoginResponseDto(
    val token: String, 
    val id: Int,
    val name: String,
    val email: String,
    val role: String
)