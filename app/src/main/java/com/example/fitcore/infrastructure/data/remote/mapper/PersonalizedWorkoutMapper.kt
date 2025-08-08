package com.example.fitcore.infrastructure.data.remote.mapper

import com.example.fitcore.domain.model.PersonalizedWorkout
import com.example.fitcore.infrastructure.data.remote.dto.PersonalizedWorkoutDto

fun PersonalizedWorkoutDto.toDomain(): PersonalizedWorkout {
    return PersonalizedWorkout(
        id = this.id,
        name = this.name,
        description = this.description,
        isPublic = this.isPublic,
        studentIds = this.studentIds,
        items = this.items.map { it.toDomain() }
    )
}
