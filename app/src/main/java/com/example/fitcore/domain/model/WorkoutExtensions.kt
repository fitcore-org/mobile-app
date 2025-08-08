package com.example.fitcore.domain.model

fun Workout.toPublicWorkout(): PublicWorkout {
    return PublicWorkout(
        id = id.toString(),
        name = title,
        description = description,
        items = emptyList(), // Since Workout doesn't have items
        isPublic = true,
        studentIds = emptyList()
    )
}

fun PersonalizedWorkout.toPublicWorkout(): PublicWorkout {
    return PublicWorkout(
        id = id,
        name = name,
        description = description,
        items = items,
        isPublic = isPublic,
        studentIds = studentIds ?: emptyList()
    )
}

fun estimateWorkoutDuration(workout: PublicWorkout): String {
    var totalMinutes = 0
    
    workout.items.forEach { item ->
        val sets = item.sets.split("-").firstOrNull()?.toIntOrNull() ?: 1
        val restSeconds = item.restSeconds
        
        // Tempo estimado por série (30s por série + descanso)
        totalMinutes += (sets * (30 + restSeconds)) / 60
    }
    
    return when {
        totalMinutes < 60 -> "$totalMinutes min"
        else -> "${totalMinutes / 60}h ${totalMinutes % 60}min"
    }
}
