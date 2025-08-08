package com.example.fitcore.application.state

import com.example.fitcore.domain.model.PublicWorkout

object WorkoutSelectionManager {
    private var selectedWorkout: PublicWorkout? = null
    
    fun setSelectedWorkout(workout: PublicWorkout) {
        selectedWorkout = workout
    }
    
    fun getSelectedWorkout(): PublicWorkout? {
        return selectedWorkout
    }
    
    fun clearSelectedWorkout() {
        selectedWorkout = null
    }
}
