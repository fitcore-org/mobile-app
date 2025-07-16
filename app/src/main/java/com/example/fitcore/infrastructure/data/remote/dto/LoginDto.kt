package com.example.fitcore.infrastructure.data.remote.dto
data class LoginRequestDto(val email: String, val password: String)
data class LoginResponseDto(val token: String, val user: UserDto)