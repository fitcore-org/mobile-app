package com.example.fitcore.domain.model

/**
 * Wrapper para exercícios com tradução
 */
data class TranslatedExercise(
    val id: String,
    val originalName: String,
    val translatedName: String,
    val originalDescription: String,
    val translatedDescription: String,
    val muscleGroup: String,
    val equipment: String,
    val originalEquipment: String,
    val translatedEquipment: String,
    val mediaUrl: String?,
    val mediaUrl2: String?,
    val isTranslated: Boolean = false
) {
    // Getters que retornam versão traduzida se disponível
    val displayName: String get() = if (isTranslated && translatedName.isNotBlank()) translatedName else originalName
    val displayDescription: String get() = if (isTranslated && translatedDescription.isNotBlank()) translatedDescription else originalDescription
    val displayEquipment: String get() = if (isTranslated && translatedEquipment.isNotBlank()) translatedEquipment else originalEquipment
    
    companion object {
        fun fromExercise(exercise: Exercise): TranslatedExercise {
            return TranslatedExercise(
                id = exercise.id,
                originalName = exercise.name,
                translatedName = "",
                originalDescription = exercise.description,
                translatedDescription = "",
                muscleGroup = exercise.muscleGroup,
                equipment = exercise.equipment,
                originalEquipment = exercise.equipment,
                translatedEquipment = "",
                mediaUrl = exercise.mediaUrl,
                mediaUrl2 = exercise.mediaUrl2,
                isTranslated = false
            )
        }
    }
}
