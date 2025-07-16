package com.example.fitcore.application.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitcore.domain.model.TrainingPlan
import com.example.fitcore.domain.usecase.GetCurrentTrainingPlanUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DashboardUiState {
    object Loading : DashboardUiState()
    data class Success(val trainingPlan: TrainingPlan) : DashboardUiState()
    object NoPlan : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

class DashboardViewModel(private val getCurrentTrainingPlanUseCase: GetCurrentTrainingPlanUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadCurrentPlan()
    }

    fun loadCurrentPlan() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            val plan = getCurrentTrainingPlanUseCase()
            _uiState.value = if (plan != null) {
                DashboardUiState.Success(plan)
            } else {
                DashboardUiState.NoPlan
            }
        }
    }
}