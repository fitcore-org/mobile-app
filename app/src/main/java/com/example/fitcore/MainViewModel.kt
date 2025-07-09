package com.example.fitcore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope // Importante
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch // Importante

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow("A carregar...") // Estado inicial
    val uiState: StateFlow<String> = _uiState.asStateFlow()

    init {
        loadUserName()
    }

    private fun loadUserName() {
        // Inicia uma nova corrotina no "escopo" do ViewModel.
        // Esta corrotina será cancelada automaticamente quando o ViewModel for destruído.
        viewModelScope.launch {
            // Chama a suspend fun do repositório
            val userName = userRepository.getUserName()
            // Atualiza o estado da UI com o resultado
            _uiState.value = userName
        }
    }
}