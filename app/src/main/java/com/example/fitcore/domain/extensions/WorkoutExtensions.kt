package com.example.fitcore.domain.extensions

import com.example.fitcore.domain.model.*

fun PersonalizedWorkout.toPublicWorkout(): PublicWorkout {
    return PublicWorkout(
        id = this.id,
        name = this.name, 
        description = this.description,
        isPublic = this.isPublic,
        items = this.items,
        studentIds = this.studentIds ?: emptyList()
    )
}

fun Workout.toPublicWorkout(): PublicWorkout {
    // Convertendo workouts antigos para o novo formato
    // Como o Workout antigo não tem a lista de items,
    // criamos um workout público vazio que será atualizado 
    // quando o usuário acessar os detalhes
    return PublicWorkout(
        id = this.id.toString(),
        name = this.title,
        description = this.description,
        isPublic = true, // workouts antigos são sempre públicos
        items = emptyList(), // será carregado posteriormente
        studentIds = emptyList() // workout antigo não tem alunos associados
    )
}
