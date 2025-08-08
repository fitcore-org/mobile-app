package com.example.fitcore.infrastructure.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PublicWorkoutDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("isPublic") val isPublic: Boolean,
    @SerializedName("items") val items: List<WorkoutItemDto>,
    @SerializedName("studentIds") val studentIds: List<String>
)
