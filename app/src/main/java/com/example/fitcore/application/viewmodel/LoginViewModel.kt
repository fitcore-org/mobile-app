package com.example.fitcore.application.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitcore.domain.model.User
import com.example.fitcore.domain.repository.LoginResult
import com.example.fitcore.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
sealed class LoginUiState {
    object Idle : LoginUiState(); object Loading : LoginUiState(); data class Success(val user: User) : LoginUiState(); data class Error(val message: String) : LoginUiState()
}
class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            when (val result = loginUseCase(email, password)) {
                is LoginResult.Success -> _uiState.value = LoginUiState.Success(result.user)
                is LoginResult.InvalidCredentials -> _uiState.value = LoginUiState.Error("Credenciais inválidas.")
                is LoginResult.GenericError -> _uiState.value = LoginUiState.Error(result.message)
            }
        }
    }
}