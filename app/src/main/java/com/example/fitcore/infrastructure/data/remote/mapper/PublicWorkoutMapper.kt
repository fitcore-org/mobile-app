package com.example.fitcore.infrastructure.data.remote.mapper

import com.example.fitcore.domain.model.PublicWorkout
import com.example.fitcore.infrastructure.data.remote.dto.PublicWorkoutDto

fun PublicWorkoutDto.toDomain(): PublicWorkout {
    return PublicWorkout(
        id = this.id,
        name = this.name,
        description = this.description,
        isPublic = this.isPublic,
        items = this.items.map { it.toDomain() },
        studentIds = this.studentIds
    )
}
