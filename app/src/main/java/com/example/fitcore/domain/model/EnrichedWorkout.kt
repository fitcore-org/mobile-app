package com.example.fitcore.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EnrichedWorkout(
    val id: String,
    val name: String,
    val description: String,
    val isPublic: Boolean,
    val items: List<WorkoutItem>
) : Parcelable
