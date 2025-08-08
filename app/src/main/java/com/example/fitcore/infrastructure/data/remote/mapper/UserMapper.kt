package com.example.fitcore.infrastructure.data.remote.mapper

import com.example.fitcore.domain.model.User
import com.example.fitcore.infrastructure.data.remote.dto.StudentResponseDto

/**
 * Converte o Data Transfer Object (DTO) da resposta da API de estudante
 * para o modelo de domínio 'User', que é usado em toda a aplicação (UI, ViewModels).
 */
/**
 * Converte uma URL que usa localhost para usar o IP do emulador Android.
 */
private fun convertUrlForAndroid(url: String?): String? {
    return url?.replace("localhost", "10.0.2.2")
}

fun StudentResponseDto.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        email = this.email,
        // O campo 'username' não existe no DTO, então usamos o email como um padrão.
        // Ajuste se necessário.
        username = this.email, 
        phone = this.phone,
        cpf = this.cpf,
        birthDate = this.birthDate,
        height = this.height,
        weight = this.weight,
        plan = this.plan,
        photoUrl = convertUrlForAndroid(this.photoUrl)
    )
}