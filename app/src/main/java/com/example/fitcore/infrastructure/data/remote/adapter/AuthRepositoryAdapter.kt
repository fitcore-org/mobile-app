package com.example.fitcore.infrastructure.data.remote.adapter
import com.example.fitcore.domain.model.User
import com.example.fitcore.domain.repository.AuthRepositoryPort
import com.example.fitcore.domain.repository.LoginResult
import kotlinx.coroutines.delay

class AuthRepositoryAdapter : AuthRepositoryPort {
    override suspend fun login(email: String, password: String): LoginResult {
        delay(1500)
        return if (email == "aluno@fitcore.com" && password == "123456") {
            val fakeUser = User(id = 1, name = "Silas Santos", username = "silas.smfs", email = "aluno@fitcore.com")
            val fakeToken = "fake-jwt-token-for-testing-offline"
            LoginResult.Success(fakeToken, fakeUser)
        } else {
            LoginResult.InvalidCredentials
        }
    }
}