package com.example.fitcore.infrastructure.data.local

/**
 * Dicionário de traduções estáticas para exercícios
 * Solução eficiente, instantânea e ilimitada
 */
object ExerciseTranslationDictionary {
    
    // Traduções de nomes de exercícios
    private val exerciseNames = mapOf(
        "3/4 Sit-Up" to "3/4 Abdominal",
        "90/90 Hamstring" to "90/90 Isquiotibiais",
        "Barbell Bench Press - Medium Grip" to "Supino com Barra - Pegada Média",
        "Barbell Deadlift" to "Levantamento Terra com Barra",
        "Barbell Full Squat" to "Agachamento Completo com Barra",
        "Bodyweight Squat" to "Agachamento Livre",
        "Decline Push-Up" to "Flexão Declinada",
        "Handstand Push-Ups" to "Flexão em Parada de Mão",
        "Incline Push-Up" to "Flexão Inclinada",
        "Plyo Push-up" to "Flexão Pliométrica",
        
        // Adicionar mais conforme necessário
        "Push-up" to "Flexão",
        "Pull-up" to "Barra Fixa",
        "Dumbbell Curl" to "Rosca com Halter",
        "Bench Press" to "Supino",
        "Shoulder Press" to "Desenvolvimento",
        "Lat Pulldown" to "Puxada Alta",
        "Leg Press" to "Leg Press",
        "Calf Raise" to "Panturrilha em Pé",
        "Tricep Dip" to "Mergulho para Tríceps",
        "Bicep Curl" to "Rosca Bíceps"
    )
    
    // Traduções de equipamentos
    private val equipmentTranslations = mapOf(
        "body only" to "Apenas corpo",
        "barbell" to "Barra",
        "dumbbell" to "Halter",
        "cable" to "Cabo/Polia",
        "machine" to "Máquina",
        "kettlebell" to "Kettlebell",
        "resistance band" to "Faixa elástica",
        "medicine ball" to "Bola medicinal",
        "foam roll" to "Rolo de espuma",
        "exercise ball" to "Bola suíça",
        "other" to "Outro",
        "Não especificado" to "Não especificado"
    )
    
    // Fragmentos comuns para descrições
    private val descriptionFragments = mapOf(
        // Posições iniciais
        "Lie down on the floor" to "Deite-se no chão",
        "Stand with your feet shoulder width apart" to "Fique em pé com os pés na largura dos ombros",
        "Lie back on a flat bench" to "Deite-se de costas em um banco plano",
        "Stand in front of" to "Fique em frente a",
        "Place your hands" to "Coloque suas mãos",
        "Hold the bar" to "Segure a barra",
        "Grasp the bar" to "Segure a barra",
        
        // Movimentos
        "Lower yourself" to "Abaixe-se",
        "Push yourself back up" to "Empurre-se de volta para cima",
        "Raise your torso" to "Levante seu tronco",
        "Flex your hips and spine" to "Flexione seus quadris e coluna",
        "Breathe in" to "Inspire",
        "Breathe out" to "Expire", 
        "Inhale" to "Inspire",
        "Exhale" to "Expire",
        "Hold for a second" to "Segure por um segundo",
        "Pause briefly" to "Faça uma pausa breve",
        
        // Instruções gerais
        "This will be your starting position" to "Esta será sua posição inicial",
        "Return to the starting position" to "Retorne à posição inicial",
        "Repeat for the recommended amount of repetitions" to "Repita pelo número recomendado de repetições",
        "Keep your back straight" to "Mantenha as costas retas",
        "Keep your head up" to "Mantenha a cabeça erguida",
        "Focus on" to "Concentre-se em",
        "Make sure" to "Certifique-se",
        
        // Partes do corpo
        "your legs" to "suas pernas",
        "your arms" to "seus braços", 
        "your chest" to "seu peito",
        "your back" to "suas costas",
        "your shoulders" to "seus ombros",
        "your knees" to "seus joelhos",
        "your feet" to "seus pés",
        "your hands" to "suas mãos",
        "your torso" to "seu tronco",
        
        // Ações
        "bend" to "dobre",
        "extend" to "estenda",
        "stretch" to "estique",
        "contract" to "contraia",
        "relax" to "relaxe",
        "squeeze" to "aperte",
        "release" to "solte"
    )
    
    /**
     * Traduz nome do exercício
     */
    fun translateExerciseName(name: String): String {
        return exerciseNames[name] ?: name
    }
    
    /**
     * Traduz equipamento
     */
    fun translateEquipment(equipment: String): String {
        return equipmentTranslations[equipment.lowercase()] ?: equipment
    }
    
    /**
     * Traduz descrição usando fragmentos comuns
     */
    fun translateDescription(description: String): String {
        if (description.isBlank()) return description
        
        var translatedDescription = description
        
        // Aplica traduções de fragmentos comuns
        descriptionFragments.forEach { (english, portuguese) ->
            translatedDescription = translatedDescription.replace(
                english, portuguese, ignoreCase = true
            )
        }
        
        return translatedDescription
    }
    
    /**
     * Verifica se uma tradução existe para o exercício
     */
    fun hasTranslation(exerciseName: String): Boolean {
        return exerciseNames.containsKey(exerciseName)
    }
    
    /**
     * Adiciona nova tradução (para expansão futura)
     */
    fun addTranslation(originalName: String, translatedName: String) {
        // Para implementação futura com cache persistente
    }
}
