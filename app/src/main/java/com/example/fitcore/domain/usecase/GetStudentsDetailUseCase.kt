package com.example.fitcore.domain.usecase

import com.example.fitcore.domain.model.User
import com.example.fitcore.domain.repository.StudentRepositoryPort

class GetStudentDetailsUseCase(private val studentRepository: StudentRepositoryPort) {
    suspend operator fun invoke(basicUser: User): User {
        val studentDetails = studentRepository.getStudentByEmail(basicUser.email)
        
        // Retorna o usuário enriquecido se os detalhes forem encontrados
        return studentDetails?.let { details ->
            basicUser.copy(
                // O ID, nome e email vêm dos detalhes, que são a fonte da verdade
                id = details.id,
                name = details.name,
                email = details.email,
                // Mapeamento dos campos corrigidos
                cpf = details.cpf,
                phone = details.phone,
                birthDate = details.birthDate,
                height = details.height,
                weight = details.weight,
                plan = details.plan,
                photoUrl = details.photoUrl
            )
        } ?: basicUser // Se não encontrar, retorna o usuário básico que recebeu
    }
}