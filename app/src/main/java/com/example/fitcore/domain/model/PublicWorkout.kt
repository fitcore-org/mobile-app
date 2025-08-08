package com.example.fitcore.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PublicWorkout(
    val id: String,
    val name: String,
    val description: String,
    val isPublic: Boolean,
    val items: List<WorkoutItem>,
    val studentIds: List<String>
) : Parcelable {
    val exerciseCount: Int
        get() = items.size
    
    val estimatedDuration: String
        get() = "${items.size * 10}-${items.size * 15} min"
}
