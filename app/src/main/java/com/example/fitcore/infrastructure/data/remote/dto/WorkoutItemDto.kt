package com.example.fitcore.infrastructure.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WorkoutItemDto(
    @SerializedName("id") val id: String,
    @SerializedName("exercise") val exercise: ExerciseDto,
    @SerializedName("sets") val sets: String,
    @SerializedName("reps") val reps: String,
    @SerializedName("restSeconds") val restSeconds: Int,
    @SerializedName("observation") val observation: String?,
    @SerializedName("order") val order: Int
)
