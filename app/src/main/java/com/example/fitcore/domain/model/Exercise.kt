package com.example.fitcore.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Exercise(
    val id: String,
    val name: String,
    val description: String,
    val muscleGroup: String,
    val equipment: String,
    val mediaUrl: String?,
    val mediaUrl2: String?
) : Parcelable

data class ExerciseLibrary(
    val exercises: List<Exercise>
)

enum class MuscleGroup(val displayName: String, val emoji: String) {
    CHEST("Peito", "💪"),
    SHOULDERS("Ombros", "🏋️"),
    BACK("Costas", "🔥"),
    BICEPS("Bíceps", "💪"),
    TRICEPS("Tríceps", "🔥"),
    ABDOMINALS("Abdômen", "⚡"),
    QUADRICEPS("Quadríceps", "🦵"),
    HAMSTRINGS("Posterior", "🦵"),
    CALVES("Panturrilha", "🦵"),
    LOWER_BACK("Lombar", "🔥"),
    ALL("Todos", "🎯");
    
    companion object {
        fun fromApiValue(apiValue: String): MuscleGroup {
            return when (apiValue.lowercase()) {
                "chest" -> CHEST
                "shoulders" -> SHOULDERS
                "back" -> BACK
                "lower back" -> LOWER_BACK
                "biceps" -> BICEPS
                "triceps" -> TRICEPS
                "abdominals" -> ABDOMINALS
                "quadriceps" -> QUADRICEPS
                "hamstrings" -> HAMSTRINGS
                "calves" -> CALVES
                else -> ALL
            }
        }
    }
}