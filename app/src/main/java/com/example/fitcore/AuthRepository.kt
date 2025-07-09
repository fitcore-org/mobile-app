package com.example.fitcore
import kotlinx.coroutines.delay

sealed class LoginResult {
    data class Success(val user: User) : LoginResult()
    object InvalidCredentials : LoginResult()
}

class AuthRepository {
    // Lógica de login refatorada para ser 100% offline e previsível
    suspend fun login(email: String, password: String): LoginResult {
        delay(1500) // Simula o tempo de espera da rede

        return if (email == "aluno@fitcore.com" && password == "123456") {
            // Se o login for bem-sucedido, devolve um utilizador FALSO sem chamar a API.
            val fakeUser = User(id = 1, name = "Aluno FitCore", username = "alunofitcore", email = "aluno@fitcore.com")
            LoginResult.Success(fakeUser)
        } else {
            // Se as credenciais estiverem erradas, devolve erro.
            LoginResult.InvalidCredentials
        }
    }
}