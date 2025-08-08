package com.example.fitcore.domain.usecase

import com.example.fitcore.domain.repository.AuthRepositoryPort
import com.example.fitcore.domain.repository.LoginResult

// ATUALIZE O CONSTRUTOR PARA RECEBER OS DOIS CASOS DE USO
class LoginUseCase(
    private val authRepository: AuthRepositoryPort,
    private val getStudentDetailsUseCase: GetStudentDetailsUseCase // Adicionado
) {
    suspend operator fun invoke(email: String, password: String): LoginResult {
        if (email.isBlank() || password.isBlank()) {
            return LoginResult.GenericError("Email e senha são obrigatórios.")
        }
        
        // Mantém a lógica de login original
        val loginResult = authRepository.login(email, password)

        // Se o login for um sucesso, busca os detalhes adicionais
        return when (loginResult) {
            is LoginResult.Success -> {
                // Pega o usuário básico retornado pelo login
                val basicUser = loginResult.user
                // Enriquece o usuário com os detalhes do outro serviço
                val fullUser = getStudentDetailsUseCase(basicUser)
                // Retorna um novo Success com o token e o usuário completo
                LoginResult.Success(token = loginResult.token, user = fullUser)
            }
            else -> {
                // Se o login falhou, retorna o resultado de falha original
                loginResult
            }
        }
    }
}