package com.example.fitcore.application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitcore.domain.model.TrainingPlan
import com.example.fitcore.domain.model.User
import com.example.fitcore.domain.usecase.GetCurrentTrainingPlanUseCase
import com.example.fitcore.domain.usecase.GetStudentDetailsUseCase // <-- ADICIONADO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log // <-- ADICIONADO

// --- ESTADO DA UI MODIFICADO ---
// Agora o 'Success' carrega o usuário e um plano de treino que pode ser nulo.
sealed class DashboardUiState {
    object Loading : DashboardUiState()
    data class Success(val user: User, val trainingPlan: TrainingPlan?) : DashboardUiState()
    // O 'NoPlan' não é mais necessário, pois 'trainingPlan' pode ser nulo.
    data class Error(val message: String) : DashboardUiState()
}

// --- CONSTRUTOR MODIFICADO ---
// Adicionamos as dependências para buscar os dados do usuário.
class DashboardViewModel(
    private val getCurrentTrainingPlanUseCase: GetCurrentTrainingPlanUseCase,
    private val getStudentDetailsUseCase: GetStudentDetailsUseCase, // <-- ADICIONADO
    private val basicUser: User // <-- ADICIONADO
) : ViewModel() {
    
    // O 'StateFlow' agora usa o novo 'DashboardUiState'.
    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        // Agora chamamos uma função que carrega TUDO.
        loadDashboardData()
    }

    // --- NOVA FUNÇÃO ADICIONADA ---
    // Esta função carrega tanto os dados do usuário quanto o plano de treino.
    fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            try {
                // 1. Busca os detalhes do usuário
                val userDetails = getStudentDetailsUseCase(basicUser)
                
                // 2. Busca o plano de treino atual
                val plan = getCurrentTrainingPlanUseCase()

                // 3. Corrige a URL da foto do usuário
                val correctedPhotoUrl = userDetails.photoUrl?.replace("localhost", "10.0.2.2")
                val correctedUser = userDetails.copy(photoUrl = correctedPhotoUrl)

                // 4. Emite o estado de sucesso com o usuário e o plano
                _uiState.value = DashboardUiState.Success(correctedUser, plan)

            } catch (e: Exception) {
                Log.e("DashboardViewModel", "Error loading dashboard data", e)
                _uiState.value = DashboardUiState.Error("Não foi possível carregar os dados")
            }
        }
    }

    // --- SUA FUNÇÃO ANTIGA (NÃO FOI ALTERADA, APENAS NÃO É MAIS CHAMADA NO INIT) ---
    fun loadCurrentPlan() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            val plan = getCurrentTrainingPlanUseCase()
            // Esta parte da lógica foi movida para a nova função 'loadDashboardData'.
            // Para evitar quebrar algo, deixamos a função aqui.
        }
    }
}