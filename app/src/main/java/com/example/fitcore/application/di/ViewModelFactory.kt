package com.example.fitcore.application.di
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitcore.application.viewmodel.DashboardViewModel
import com.example.fitcore.application.viewmodel.LoginViewModel
import com.example.fitcore.application.viewmodel.WorkoutViewModel
import com.example.fitcore.domain.usecase.GetCurrentTrainingPlanUseCase
import com.example.fitcore.domain.usecase.GetUserWorkoutsUseCase
import com.example.fitcore.domain.usecase.LoginUseCase
import com.example.fitcore.infrastructure.data.remote.adapter.AuthRepositoryAdapter
import com.example.fitcore.infrastructure.data.remote.adapter.WorkoutRepositoryAdapter
object ViewModelFactoryProvider {
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
}