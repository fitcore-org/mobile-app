package com.example.fitcore.infrastructure.data.remote.adapter

import com.example.fitcore.domain.model.User
import com.example.fitcore.domain.repository.AuthRepositoryPort
import com.example.fitcore.domain.repository.LoginResult
import com.example.fitcore.infrastructure.data.remote.api.RetrofitInstance
import com.example.fitcore.infrastructure.data.remote.dto.LoginRequestDto
import retrofit2.HttpException
import java.io.IOException

class AuthRepositoryAdapter : AuthRepositoryPort {
    private val authApi = RetrofitInstance.authApi
    
    override suspend fun login(email: String, password: String): LoginResult {
        return try {
            val request = LoginRequestDto(email = email, password = password)
            val response = authApi.login(request)
            
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    val user = User(
                        id = loginResponse.id.toString(), // Convert Int to String
                        name = loginResponse.name,
                        username = loginResponse.email.substringBefore("@"), // Usando email como base para username
                        email = loginResponse.email,
                        phone = "",
                        cpf = "",
                        birthDate = "",
                        height = 0,
                        weight = 0,
                        plan = "",
                        photoUrl = ""
                    )
                    LoginResult.Success(token = loginResponse.token, user = user)
                } ?: LoginResult.GenericError("Erro na resposta do servidor")
            } else {
                when (response.code()) {
                    401 -> LoginResult.InvalidCredentials
                    400 -> LoginResult.InvalidCredentials
                    else -> LoginResult.GenericError("Erro no servidor: ${response.code()}")
                }
            }
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> LoginResult.InvalidCredentials
                400 -> LoginResult.InvalidCredentials
                else -> LoginResult.GenericError("Erro de rede: ${e.message}")
            }
        } catch (e: IOException) {
            LoginResult.GenericError("Erro de conexão. Verifique sua internet.")
        } catch (e: Exception) {
            LoginResult.GenericError("Erro inesperado: ${e.message}")
        }
    }
}