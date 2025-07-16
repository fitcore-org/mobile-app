package com.example.fitcore.infrastructure.data.remote.mapper
import com.example.fitcore.domain.model.TrainingPlan
import com.example.fitcore.infrastructure.data.remote.dto.TrainingPlanDto
fun TrainingPlanDto.toDomain(): TrainingPlan { return TrainingPlan(id = this.id, name = this.name, workouts = this.workout_templates.map { it.toDomain() }) }
