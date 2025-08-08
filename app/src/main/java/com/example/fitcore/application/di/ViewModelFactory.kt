package com.example.fitcore.application.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitcore.application.viewmodel.*
import com.example.fitcore.domain.model.PublicWorkout
import com.example.fitcore.domain.model.User
import com.example.fitcore.domain.repository.AuthRepositoryPort
import com.example.fitcore.domain.repository.EnrichedWorkoutRepositoryPort
import com.example.fitcore.domain.repository.ExerciseRepositoryPort
import com.example.fitcore.domain.repository.PublicWorkoutRepositoryPort
import com.example.fitcore.domain.repository.StudentRepositoryPort
import com.example.fitcore.domain.repository.WorkoutRepositoryPort
import com.example.fitcore.domain.usecase.*
import com.example.fitcore.infrastructure.data.remote.adapter.*

object ViewModelFactoryProvider {

    // --- INSTÂNCIA ÚNICA DOS REPOSITÓRIOS ---
    private val authRepository: AuthRepositoryPort by lazy { AuthRepositoryAdapter() }
    private val studentRepository: StudentRepositoryPort by lazy { StudentRepositoryAdapter() }
    private val workoutRepository: WorkoutRepositoryPort by lazy { WorkoutRepositoryAdapter() }
    private val enrichedWorkoutRepository: EnrichedWorkoutRepositoryPort by lazy { EnrichedWorkoutRepositoryAdapter() }
    private val exerciseRepository: ExerciseRepositoryPort by lazy { ExerciseRepositoryAdapter() }
    private val publicWorkoutRepository: PublicWorkoutRepositoryPort by lazy { PublicWorkoutRepositoryAdapter() }

    // --- INSTÂNCIA ÚNICA DOS CASOS DE USO ---
    private val getStudentDetailsUseCase by lazy { GetStudentDetailsUseCase(studentRepository) }
    private val updateStudentUseCase: UpdateStudentUseCase by lazy { UpdateStudentUseCase(studentRepository) }
    private val loginUseCase by lazy { LoginUseCase(authRepository, getStudentDetailsUseCase) }
    private val getCurrentTrainingPlanUseCase by lazy { GetCurrentTrainingPlanUseCase(workoutRepository) }
    private val getChestWorkoutUseCase by lazy { GetChestWorkoutUseCase(enrichedWorkoutRepository) }
    private val getUserWorkoutsUseCase by lazy { GetUserWorkoutsUseCase(workoutRepository) }
    private val getPublicWorkoutsUseCase by lazy { GetPublicWorkoutsUseCase(publicWorkoutRepository) }
    private val getPublicWorkoutByIdUseCase by lazy { GetPublicWorkoutByIdUseCase(publicWorkoutRepository) }

    // --- FÁBRICAS PARA CADA VIEWMODEL ---

    fun provideUserStateViewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(UserStateViewModel::class.java)) {
                    return UserStateViewModel() as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }

    fun provideProfileViewModelFactory(user: User): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                    return ProfileViewModel(getStudentDetailsUseCase, updateStudentUseCase, user) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }

    fun provideLoginViewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                    return LoginViewModel(loginUseCase) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }

    // --- FUNÇÃO CORRIGIDA ---
    // Agora a factory aceita o 'user' para passar ao ViewModel.
    fun provideDashboardViewModelFactory(user: User): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                    // Passa todas as dependências necessárias para o DashboardViewModel
                    return DashboardViewModel(
                        getCurrentTrainingPlanUseCase = getCurrentTrainingPlanUseCase,
                        getStudentDetailsUseCase = getStudentDetailsUseCase,
                        basicUser = user
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }

    fun provideWorkoutViewModelFactory(userId: Int): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
                    return WorkoutViewModel(getUserWorkoutsUseCase, userId) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }

    fun provideWorkoutExecutionViewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(WorkoutExecutionViewModel::class.java)) {
                    return WorkoutExecutionViewModel(getChestWorkoutUseCase) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }

    fun provideExerciseLibraryViewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ExerciseLibraryViewModel::class.java)) {
                    return ExerciseLibraryViewModel(exerciseRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }

    fun providePublicWorkoutViewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PublicWorkoutViewModel::class.java)) {
                    return PublicWorkoutViewModel(getPublicWorkoutsUseCase) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }

    fun providePublicWorkoutExecutionViewModelFactory(workoutId: String): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PublicWorkoutExecutionViewModel::class.java)) {
                    return PublicWorkoutExecutionViewModel(getPublicWorkoutByIdUseCase, workoutId) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }

    fun provideWorkoutExecutionFromDataViewModelFactory(workout: PublicWorkout): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(WorkoutExecutionFromDataViewModel::class.java)) {
                    return WorkoutExecutionFromDataViewModel(workout) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}