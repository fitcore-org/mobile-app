package com.example.fitcore.domain.repository

import com.example.fitcore.domain.model.PersonalizedWorkout

interface PersonalizedWorkoutRepositoryPort {
    suspend fun getPersonalizedWorkouts(studentId: String): List<PersonalizedWorkout>
}
