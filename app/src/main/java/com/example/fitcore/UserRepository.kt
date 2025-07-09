package com.example.fitcore

// A função agora é uma `suspend fun`, pois operações de rede são assíncronas.
class UserRepository {
    suspend fun getUserName(): String {
        try {
            // Chama a nossa API para buscar o utilizador com id 1.
            val user = RetrofitInstance.api.getUser(1)
            // Devolve o nome do utilizador obtido.
            return user.name
        } catch (e: Exception) {
            // Em caso de erro de rede, devolve uma mensagem de erro.
            return "Erro ao carregar dados"
        }
    }
}