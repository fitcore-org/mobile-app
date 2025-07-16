package com.example.fitcore.domain.usecase

import com.example.fitcore.domain.repository.AuthRepositoryPort
import com.example.fitcore.domain.repository.LoginResult

class LoginUseCase(private val authRepository: AuthRepositoryPort) {
    suspend operator fun invoke(email: String, password: String): LoginResult {
        if (email.isBlank() || password.isBlank()) {
            return LoginResult.GenericError("Email e senha são obrigatórios.")
        }
        return authRepository.login(email, password)
    }
}