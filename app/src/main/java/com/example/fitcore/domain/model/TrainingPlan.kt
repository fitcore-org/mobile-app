package com.example.fitcore.domain.model

data class TrainingPlan(
    val id: Int,
    val name: String,
    val workouts: List<Workout>
)