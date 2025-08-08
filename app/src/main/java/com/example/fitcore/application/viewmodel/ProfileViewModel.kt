package com.example.fitcore.application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitcore.domain.model.User
import com.example.fitcore.domain.usecase.GetStudentDetailsUseCase
import com.example.fitcore.domain.usecase.UpdateStudentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import java.io.File

// --- Estado da UI ---
sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(
        val user: User,
        val isEditing: Boolean = false,
        val editField: String? = null,
        val isSaving: Boolean = false,
        val isUploadingPhoto: Boolean = false,
        val snackbarMessage: String? = null
    ) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

// --- Operações de Foto ---
sealed class PhotoOperation {
    data class Upload(val file: File) : PhotoOperation()
    data class Update(val file: File) : PhotoOperation()
}

// --- Campos Editáveis ---
sealed class EditableField(val key: String) {
    object Name : EditableField("name")
    object Phone : EditableField("phone")
    object Cpf : EditableField("cpf")
    object BirthDate : EditableField("birthDate")
    object Height : EditableField("height")
    object Weight : EditableField("weight")
}

class ProfileViewModel(
    private val getStudentDetailsUseCase: GetStudentDetailsUseCase,
    private val updateStudentUseCase: UpdateStudentUseCase,
    private val basicUser: User
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserDetails()
    }

    fun loadUserDetails() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                // 1. Busca os detalhes do usuário
                val userDetails = getStudentDetailsUseCase(basicUser)

                // 2. CORRIGE A URL 'localhost' PARA O IP DO EMULADOR
                val correctedPhotoUrl = userDetails.photoUrl?.replace("localhost", "10.0.2.2")
                
                // 3. Cria um novo objeto User com a URL corrigida
                val correctedUser = userDetails.copy(photoUrl = correctedPhotoUrl)

                // 4. Atualiza a tela com os dados e URL corrigidos
                _uiState.value = ProfileUiState.Success(correctedUser)

            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error loading user details", e)
                _uiState.value = ProfileUiState.Error("Não foi possível carregar os detalhes do usuário")
            }
        }
    }

    fun startEditing(field: EditableField) {
        val currentState = _uiState.value
        if (currentState is ProfileUiState.Success) {
            _uiState.value = currentState.copy(
                isEditing = true,
                editField = field.key
            )
        }
    }

    fun cancelEditing() {
        val currentState = _uiState.value
        if (currentState is ProfileUiState.Success) {
            _uiState.value = currentState.copy(
                isEditing = false,
                editField = null
            )
        }
    }
    
    fun dismissSnackbar() {
        val currentState = _uiState.value
        if (currentState is ProfileUiState.Success) {
            _uiState.value = currentState.copy(snackbarMessage = null)
        }
    }

    fun updateField(field: EditableField, value: String) {
        val currentState = _uiState.value
        if (currentState !is ProfileUiState.Success) return

        viewModelScope.launch {
            try {
                _uiState.value = currentState.copy(isSaving = true)

                val updates: Map<String, Any> = when (field) {
                    is EditableField.Name -> mapOf("name" to value)
                    is EditableField.Phone -> mapOf("phone" to value)
                    is EditableField.Cpf -> mapOf("cpf" to value)
                    is EditableField.BirthDate -> mapOf("birthDate" to value)
                    is EditableField.Height -> {
                        val heightValue = value.toIntOrNull()
                        if (heightValue == null) {
                            _uiState.value = currentState.copy(isSaving = false, snackbarMessage = "Altura inválida.")
                            return@launch
                        }
                        mapOf("height" to heightValue)
                    }
                    is EditableField.Weight -> {
                        val weightValue = value.toIntOrNull()
                        if (weightValue == null) {
                            _uiState.value = currentState.copy(isSaving = false, snackbarMessage = "Peso inválido.")
                            return@launch
                        }
                        mapOf("weight" to weightValue)
                    }
                }

                val updatedUserResponse = updateStudentUseCase(currentState.user.id, updates)

                if (updatedUserResponse != null) {
                    // Após atualizar, a URL do backend ainda virá com "localhost", então corrigimos de novo
                    val correctedUrl = updatedUserResponse.photoUrl?.replace("localhost", "10.0.2.2")
                    val finalUser = updatedUserResponse.copy(photoUrl = correctedUrl)

                    _uiState.value = ProfileUiState.Success(
                        user = finalUser,
                        isEditing = false,
                        isSaving = false,
                        snackbarMessage = "${field.key.replaceFirstChar { it.uppercase() }} atualizado com sucesso!"
                    )
                } else {
                    _uiState.value = currentState.copy(
                        isSaving = false,
                        isEditing = false,
                        snackbarMessage = "Falha ao atualizar. Tente novamente."
                    )
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error updating field", e)
                _uiState.value = currentState.copy(
                    isSaving = false,
                    isEditing = false,
                    snackbarMessage = "Erro: ${e.message}"
                )
            }
        }
    }

    fun handlePhotoOperation(operation: PhotoOperation) {
        val currentState = _uiState.value
        if (currentState !is ProfileUiState.Success) return

        viewModelScope.launch {
            try {
                _uiState.value = currentState.copy(isUploadingPhoto = true)

                val updatedUserResponse = when (operation) {
                    is PhotoOperation.Upload -> updateStudentUseCase.uploadProfilePhoto(currentState.user.id, operation.file)
                    is PhotoOperation.Update -> updateStudentUseCase.updateProfilePhoto(currentState.user.id, operation.file)
                }

                if (updatedUserResponse != null) {
                    // Após o upload, a nova URL também precisa ser corrigida
                    val correctedUrl = updatedUserResponse.photoUrl?.replace("localhost", "10.0.2.2")
                    val finalUser = updatedUserResponse.copy(photoUrl = correctedUrl)

                    _uiState.value = ProfileUiState.Success(
                        user = finalUser,
                        isUploadingPhoto = false,
                        snackbarMessage = "Foto de perfil atualizada com sucesso!"
                    )
                } else {
                    _uiState.value = currentState.copy(
                        isUploadingPhoto = false,
                        snackbarMessage = "Falha ao atualizar a foto. Tente novamente."
                    )
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error handling photo operation", e)
                _uiState.value = currentState.copy(
                    isUploadingPhoto = false,
                    snackbarMessage = "Erro ao processar a foto: ${e.message}"
                )
            }
        }
    }
}