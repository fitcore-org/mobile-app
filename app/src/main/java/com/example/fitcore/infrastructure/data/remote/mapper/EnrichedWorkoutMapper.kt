package com.example.fitcore.infrastructure.data.remote.mapper

import com.example.fitcore.domain.model.Exercise
import com.example.fitcore.domain.model.WorkoutItem
import com.example.fitcore.domain.model.EnrichedWorkout
import com.example.fitcore.infrastructure.data.remote.dto.ExerciseDto
import com.example.fitcore.infrastructure.data.remote.dto.WorkoutItemDto
import com.example.fitcore.infrastructure.data.remote.dto.EnrichedWorkoutDto

fun ExerciseDto.toDomain(): Exercise {
    return Exercise(
        id = this.id,
        name = this.name,
        description = this.description,
        muscleGroup = this.muscleGroup,
        equipment = this.equipment,
        mediaUrl = this.mediaUrl,
        mediaUrl2 = this.mediaUrl2
    )
}

fun WorkoutItemDto.toDomain(): WorkoutItem {
    return WorkoutItem(
        id = this.id,
        exercise = this.exercise.toDomain(),
        sets = this.sets,
        reps = this.reps,
        restSeconds = this.restSeconds,
        observation = this.observation,
        order = this.order
    )
}

fun EnrichedWorkoutDto.toDomain(): EnrichedWorkout {
    return EnrichedWorkout(
        id = this.id,
        name = this.name,
        description = this.description,
        isPublic = this.isPublic,
        items = this.items.map { it.toDomain() }
    )
}
