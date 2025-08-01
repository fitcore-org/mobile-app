package com.example.fitcore.infrastructure.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ExerciseDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("muscleGroup") val muscleGroup: String,
    @SerializedName("equipment") val equipment: String,
    @SerializedName("mediaUrl") val mediaUrl: String?,
    @SerializedName("mediaUrl2") val mediaUrl2: String?
)