package com.example.fitcore.application.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitcore.application.viewmodel.DashboardViewModel
import com.example.fitcore.application.viewmodel.LoginViewModel
import com.example.fitcore.application.viewmodel.WorkoutViewModel
import com.example.fitcore.application.viewmodel.WorkoutExecutionViewModel
import com.example.fitcore.application.viewmodel.ExerciseLibraryViewModel
import com.example.fitcore.domain.usecase.GetCurrentTrainingPlanUseCase
import com.example.fitcore.domain.usecase.GetUserWorkoutsUseCase
import com.example.fitcore.domain.usecase.LoginUseCase
import com.example.fitcore.domain.usecase.GetChestWorkoutUseCase
import com.example.fitcore.infrastructure.data.remote.adapter.AuthRepositoryAdapter
import com.example.fitcore.infrastructure.data.remote.adapter.WorkoutRepositoryAdapter
import com.example.fitcore.infrastructure.data.remote.adapter.EnrichedWorkoutRepositoryAdapter
import com.example.fitcore.infrastructure.data.remote.adapter.ExerciseRepositoryAdapter
import com.example.fitcore.infrastructure.data.remote.api.RetrofitInstance

object ViewModelFactoryProvider {
    val Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when (modelClass) {
                LoginViewModel::class.java -> {
                    val authRepository = AuthRepositoryAdapter()
                    val loginUseCase = LoginUseCase(authRepository)
                    LoginViewModel(loginUseCase) as T
                }
                DashboardViewModel::class.java -> {
                    val workoutRepository = WorkoutRepositoryAdapter()
                    val getCurrentTrainingPlanUseCase = GetCurrentTrainingPlanUseCase(workoutRepository)
                    DashboardViewModel(getCurrentTrainingPlanUseCase) as T
                }
                WorkoutExecutionViewModel::class.java -> {
                    val enrichedWorkoutRepository = EnrichedWorkoutRepositoryAdapter()
                    val getChestWorkoutUseCase = GetChestWorkoutUseCase(enrichedWorkoutRepository)
                    WorkoutExecutionViewModel(getChestWorkoutUseCase) as T
                }
                ExerciseLibraryViewModel::class.java -> {
                    val exerciseRepository = ExerciseRepositoryAdapter(RetrofitInstance.exerciseApi)
                    ExerciseLibraryViewModel(exerciseRepository) as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
    
    fun provideLoginViewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val authRepository = AuthRepositoryAdapter()
                val loginUseCase = LoginUseCase(authRepository)
                return LoginViewModel(loginUseCase) as T
            }
        }
    }
    
    fun provideWorkoutViewModelFactory(userId: Int): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val workoutRepository = WorkoutRepositoryAdapter()
                val getUserWorkoutsUseCase = GetUserWorkoutsUseCase(workoutRepository)
                return WorkoutViewModel(getUserWorkoutsUseCase, userId) as T
            }
        }
    }
    
    fun provideDashboardViewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val workoutRepository = WorkoutRepositoryAdapter()
                val getCurrentTrainingPlanUseCase = GetCurrentTrainingPlanUseCase(workoutRepository)
                return DashboardViewModel(getCurrentTrainingPlanUseCase) as T
            }
        }
    }
    
    fun provideWorkoutExecutionViewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val enrichedWorkoutRepository = EnrichedWorkoutRepositoryAdapter()
                val getChestWorkoutUseCase = GetChestWorkoutUseCase(enrichedWorkoutRepository)
                return WorkoutExecutionViewModel(getChestWorkoutUseCase) as T
            }
        }
    }
}