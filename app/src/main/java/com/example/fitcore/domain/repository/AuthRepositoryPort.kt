package com.example.fitcore.domain.repository

import com.example.fitcore.domain.model.User

sealed class LoginResult {
    data class Success(val token: String, val user: User) : LoginResult()
    object InvalidCredentials : LoginResult()
    data class GenericError(val message: String) : LoginResult()
}

interface AuthRepositoryPort {
    suspend fun login(email: String, password: String): LoginResult
}