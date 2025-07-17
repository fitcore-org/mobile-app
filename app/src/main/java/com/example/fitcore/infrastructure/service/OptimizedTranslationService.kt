package com.example.fitcore.infrastructure.service

import com.example.fitcore.infrastructure.data.local.ExerciseTranslationDictionary

/**
 * Serviço de tradução otimizado usando dicionário estático local.
 * Substitui o ML Kit para oferecer traduções instantâneas e sem limites de caracteres.
 */
class OptimizedTranslationService private constructor() {
    
    companion object {
        @Volatile
        private var INSTANCE: OptimizedTranslationService? = null
        
        fun getInstance(): OptimizedTranslationService {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: OptimizedTranslationService().also { INSTANCE = it }
            }
        }
    }
    
    /**
     * Traduz o nome de um exercício.
     */
    fun translateExerciseName(exerciseName: String): String {
        return ExerciseTranslationDictionary.translateExerciseName(exerciseName)
    }
    
    /**
     * Traduz o equipamento necessário.
     */
    fun translateEquipment(equipment: String): String {
        return ExerciseTranslationDictionary.translateEquipment(equipment)
    }
    
    /**
     * Traduz a descrição de um exercício.
     */
    fun translateDescription(description: String): String {
        return ExerciseTranslationDictionary.translateDescription(description)
    }
    
    /**
     * Verifica se o serviço de tradução está disponível (sempre true para dicionário local).
     */
    fun isTranslationAvailable(): Boolean = true
    
    /**
     * Limpa cache (não necessário para dicionário estático, mas mantido para compatibilidade).
     */
    fun clearCache() {
        // Não há cache para limpar com dicionário estático
    }
}
