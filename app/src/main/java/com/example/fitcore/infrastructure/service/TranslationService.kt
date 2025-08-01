package com.example.fitcore.infrastructure.service

import com.example.fitcore.infrastructure.data.local.ExerciseTranslationDictionary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Serviço de tradução otimizado usando dicionário estático
 * - Tradução instantânea (sem latência)
 * - Ilimitado (sem custos de API)
 * - 100% offline
 * - Traduções precisas e revisadas
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
    
    private val _isReady = MutableStateFlow(true) // Sempre pronto
    val isReady: StateFlow<Boolean> = _isReady
    
    /**
     * Traduz texto instantaneamente usando dicionário local
     */
    fun translateText(text: String): String {
        if (text.isBlank()) return text
        
        return when {
            // Primeiro tenta tradução exata do nome
            ExerciseTranslationDictionary.hasTranslation(text) -> {
                ExerciseTranslationDictionary.translateExerciseName(text)
            }
            // Se for descrição, usa fragmentos
            text.length > 50 -> {
                ExerciseTranslationDictionary.translateDescription(text)
            }
            // Se for equipamento
            else -> {
                ExerciseTranslationDictionary.translateEquipment(text)
            }
        }
    }
    
    /**
     * Traduz nome específico do exercício
     */
    fun translateExerciseName(name: String): String {
        return ExerciseTranslationDictionary.translateExerciseName(name)
    }
    
    /**
     * Traduz equipamento
     */
    fun translateEquipment(equipment: String): String {
        return ExerciseTranslationDictionary.translateEquipment(equipment)
    }
    
    /**
     * Traduz descrição
     */
    fun translateDescription(description: String): String {
        return ExerciseTranslationDictionary.translateDescription(description)
    }
    
    /**
     * Verifica se tradução está disponível para um exercício
     */
    fun hasTranslation(exerciseName: String): Boolean {
        return ExerciseTranslationDictionary.hasTranslation(exerciseName)
    }
    
    /**
     * Sempre verdadeiro - tradução instantânea
     */
    fun isTranslationAvailable(): Boolean = true
    
    /**
     * Não precisa de inicialização
     */
    fun initialize(): Boolean = true
}
