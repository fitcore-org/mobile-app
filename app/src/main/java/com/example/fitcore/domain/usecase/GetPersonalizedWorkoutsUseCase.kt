package com.example.fitcore.domain.usecase

import com.example.fitcore.domain.model.PersonalizedWorkout
import com.example.fitcore.domain.repository.PersonalizedWorkoutRepositoryPort

class GetPersonalizedWorkoutsUseCase(private val repository: PersonalizedWorkoutRepositoryPort) {
    suspend operator fun invoke(studentId: String): List<PersonalizedWorkout> {
        return repository.getPersonalizedWorkouts(studentId)
    }
}
