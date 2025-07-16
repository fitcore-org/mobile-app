package com.example.fitcore.infrastructure.data.remote.dto

data class ExerciseDto(
    val id: String,
    val name: String,
    val repetitions: Int,
    val sets: Int,
    val duration: Int? // se aplicável
)