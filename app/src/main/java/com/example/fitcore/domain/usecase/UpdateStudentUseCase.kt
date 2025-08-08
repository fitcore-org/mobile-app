package com.example.fitcore.domain.usecase

import com.example.fitcore.domain.model.User
import com.example.fitcore.domain.repository.StudentRepositoryPort
import com.example.fitcore.infrastructure.data.remote.mapper.toDomain
import java.io.File

class UpdateStudentUseCase(
    private val studentRepository: StudentRepositoryPort
) {
    /**
     * Invoca o caso de uso para atualizar os dados de um estudante.
     * @param id O ID do estudante a ser atualizado.
     * @param updates Um mapa com os campos e os novos valores.
     * @return O objeto 'User' atualizado, ou nulo se a atualização falhar.
     */
    suspend operator fun invoke(id: String, updates: Map<String, Any>): User? {
        // Converte todos os valores para String
        val stringUpdates = updates.mapValues { it.value.toString() }
        
        // Chama o repositório para fazer a requisição à API
        val updatedStudentDto = studentRepository.updateStudent(id, stringUpdates)
        
        // Se a resposta não for nula, converte o DTO para o modelo de domínio 'User'
        return updatedStudentDto?.toDomain()
    }

    /**
     * Faz upload de uma nova foto de perfil para o estudante.
     * @param studentId O ID do estudante.
     * @param photoFile O arquivo da foto.
     * @return O objeto 'User' atualizado, ou nulo se o upload falhar.
     */
    suspend fun uploadProfilePhoto(studentId: String, photoFile: File): User? {
        val updatedStudentDto = studentRepository.uploadProfilePhoto(studentId, photoFile)
        return updatedStudentDto?.toDomain()
    }

    /**
     * Atualiza a foto de perfil existente do estudante.
     * @param studentId O ID do estudante.
     * @param photoFile O arquivo da nova foto.
     * @return O objeto 'User' atualizado, ou nulo se a atualização falhar.
     */
    suspend fun updateProfilePhoto(studentId: String, photoFile: File): User? {
        val updatedStudentDto = studentRepository.updateProfilePhoto(studentId, photoFile)
        return updatedStudentDto?.toDomain()
    }
}