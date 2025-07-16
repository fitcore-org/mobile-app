package com.example.fitcore.infrastructure.data.remote.mapper
import com.example.fitcore.domain.model.Workout
import com.example.fitcore.infrastructure.data.remote.dto.WorkoutDto
fun WorkoutDto.toDomain(): Workout { return Workout(id = this.id, title = this.title, description = this.body) }