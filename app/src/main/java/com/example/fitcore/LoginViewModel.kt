package com.example.fitcore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
sealed class LoginUiState {
    object Idle : LoginUiState(); object Loading : LoginUiState(); data class Success(val user: User) : LoginUiState(); data class Error(val message: String) : LoginUiState()
}
class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()
    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) { _uiState.value = LoginUiState.Error("Email e senha são obrigatórios."); return }
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            // Trata os diferentes resultados possíveis vindos do repositório
            when (val result = authRepository.login(email, password)) {
                is LoginResult.Success -> { _uiState.value = LoginUiState.Success(result.user) }
                is LoginResult.InvalidCredentials -> { _uiState.value = LoginUiState.Error("Credenciais inválidas.") }
            }
        }
    }
}