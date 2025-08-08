package com.example.fitcore.infrastructure.data.remote.mapper

import com.example.fitcore.domain.model.Exercise
import com.example.fitcore.infrastructure.data.remote.dto.ExerciseDto

object ExerciseMapper {
    fun toDomain(dto: ExerciseDto): Exercise {
        return Exercise(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            muscleGroup = dto.muscleGroup,
            equipment = dto.equipment,
            mediaUrl = convertUrl(dto.mediaUrl),
            mediaUrl2 = convertUrl(dto.mediaUrl2)
        )
    }
    
    private fun convertUrl(url: String?): String? {
        // Converte localhost para o IP que o emulador pode acessar
        // MinIO está rodando na porta 9000
        return url?.replace("localhost", "10.0.2.2")
    }
}
