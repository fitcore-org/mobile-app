package com.example.fitcore.application.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.*
import androidx.compose.runtime.*
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.provider.MediaStore
import androidx.compose.ui.platform.LocalContext
import com.example.fitcore.application.utils.PermissionHelper
import com.example.fitcore.application.config.CoilConfig
import android.widget.Toast
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.fitcore.application.di.ViewModelFactoryProvider
import com.example.fitcore.application.viewmodel.EditableField
import com.example.fitcore.application.viewmodel.ProfileUiState
import com.example.fitcore.application.viewmodel.ProfileViewModel
import com.example.fitcore.domain.model.User
import kotlinx.coroutines.launch
import com.example.fitcore.application.viewmodel.PhotoOperation
import com.example.fitcore.application.utils.toTempFile
import java.io.File

@Composable
fun ProfileScreen(user: User) {
    val viewModel: ProfileViewModel = viewModel(
        factory = ViewModelFactoryProvider.provideProfileViewModelFactory(user)
    )
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    (uiState as? ProfileUiState.Success)?.snackbarMessage?.let { message ->
        LaunchedEffect(message) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.dismissSnackbar()
            }
        }
    }

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF0a0a0a), Color(0xFF1f3a26))
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent,
        modifier = Modifier.background(gradientBrush)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is ProfileUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF22c55e))
                }
                is ProfileUiState.Error -> {
                    Text(text = state.message, color = Color.White, modifier = Modifier.align(Alignment.Center))
                }
                is ProfileUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            ProfileHeader(
                                user = state.user,
                                onPhotoSelected = { file ->
                                    // Se já existe uma foto, faz update, senão faz upload
                                    val operation = if (state.user.photoUrl.isNullOrEmpty()) {
                                        PhotoOperation.Upload(file)
                                    } else {
                                        PhotoOperation.Update(file)
                                    }
                                    viewModel.handlePhotoOperation(operation)
                                }
                            )
                        }
                        item {
                            ProfileInfoSection(
                                user = state.user,
                                uiState = state,
                                onEditClick = { field -> viewModel.startEditing(field) },
                                onSave = { field, value -> viewModel.updateField(field, value) },
                                onCancel = { viewModel.cancelEditing() }
                            )
                        }
                        item { ProfilePlanSection(user = state.user) }
                    }

                    if (state.isSaving || state.isUploadingPhoto) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF22c55e))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileHeader(
    user: User,
    modifier: Modifier = Modifier,
    onPhotoSelected: (File) -> Unit = {}
) {
    // Obtém o context atual para acessar o ContentResolver
    val context = LocalContext.current

    // Launcher para seleção de imagem da galeria
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                // Converte o URI para um arquivo temporário
                uri.toTempFile(context.contentResolver)?.let { file ->
                    onPhotoSelected(file)
                }
            }
        }
    }

    // Launcher para solicitar permissões
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.all { it }) {
            // Se todas as permissões foram concedidas, abre a galeria
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            launcher.launch(intent)
        } else {
            Toast.makeText(context, "Permissão necessária para acessar a galeria", Toast.LENGTH_SHORT).show()
        }
    }

    val openGallery = {
        if (PermissionHelper.hasGalleryPermissions(context)) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            launcher.launch(intent)
        } else {
            // Solicita as permissões necessárias
            permissionLauncher.launch(PermissionHelper.getGalleryPermissions())
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            val context = LocalContext.current
            val imageLoader = remember { CoilConfig.getImageLoader(context) }
            val imageRequest = remember(user.photoUrl) { 
                CoilConfig.getImageRequest(context, user.photoUrl)
            }
            
            AsyncImage(
                model = imageRequest,
                imageLoader = imageLoader,
                contentDescription = "Foto de Perfil",
                placeholder = rememberVectorPainter(image = Icons.Default.Person),
                error = rememberVectorPainter(image = Icons.Default.Person),
                contentScale = ContentScale.Crop,
                onError = { error ->
                    // Log the error silently without crashing
                    error.result.throwable.printStackTrace()
                },
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape)
            )
            // Botão de adicionar foto sobreposto à imagem
            IconButton(
                onClick = { openGallery() },
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.BottomEnd)
                    .background(Color(0xFF22c55e), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = "Adicionar foto",
                    tint = Color.White
                )
            }
        }
        Text(
            text = user.name ?: "Nome não informado",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
        Text(
            text = user.email ?: "Email não informado",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun ProfileInfoSection(
    user: User,
    uiState: ProfileUiState.Success,
    onEditClick: (EditableField) -> Unit,
    onSave: (EditableField, String) -> Unit,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Informações Pessoais",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Divider(color = Color.White.copy(alpha = 0.2f))

            EditableProfileField("Nome", user.name ?: "", EditableField.Name, uiState, onEditClick, onSave, onCancel)
            EditableProfileField("Telefone", user.phone ?: "", EditableField.Phone, uiState, onEditClick, onSave, onCancel, KeyboardType.Phone)
            EditableProfileField("CPF", user.cpf ?: "", EditableField.Cpf, uiState, onEditClick, onSave, onCancel, KeyboardType.Number)
            EditableProfileField("Data de Nascimento", user.birthDate ?: "", EditableField.BirthDate, uiState, onEditClick, onSave, onCancel)
            EditableProfileField("Altura (cm)", user.height?.toString() ?: "", EditableField.Height, uiState, onEditClick, onSave, onCancel, KeyboardType.Number)
            EditableProfileField("Peso (kg)", user.weight?.toString() ?: "", EditableField.Weight, uiState, onEditClick, onSave, onCancel, KeyboardType.Number)
        }
    }
}

@Composable
fun EditableProfileField(
    label: String,
    value: String, // Este é o valor "verdadeiro" vindo do ViewModel
    field: EditableField,
    uiState: ProfileUiState.Success,
    onEditClick: (EditableField) -> Unit,
    onSave: (EditableField, String) -> Unit,
    onCancel: () -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    // Estado local para o campo de texto
    var editValue by remember { mutableStateOf(value) }
    val isCurrentlyEditing = uiState.isEditing && uiState.editField == field.key

    // CORREÇÃO DE ESTADO APLICADA AQUI:
    // Este efeito garante que, se o valor original (do servidor) mudar,
    // o campo de edição local seja atualizado. Isso corrige o bug de
    // não resetar o valor após uma falha de atualização.
    LaunchedEffect(value) {
        editValue = value
    }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        if (isCurrentlyEditing) {
            OutlinedTextField(
                value = editValue,
                onValueChange = { editValue = it },
                label = { Text(label) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.1f),
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color(0xFF22c55e),
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    focusedIndicatorColor = Color(0xFF22c55e),
                    unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f)
                ),
                singleLine = true
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onCancel) {
                    Text("Cancelar", color = Color.White.copy(alpha = 0.7f))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { onSave(field, editValue) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22c55e))
                ) {
                    Text("Salvar")
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.7f))
                    Text(text = value.ifEmpty { "Não informado" }, style = MaterialTheme.typography.bodyLarge, color = Color.White)
                }
                IconButton(onClick = { onEditClick(field) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar $label",
                        tint = Color(0xFF22c55e)
                    )
                }
            }
            Divider(color = Color.White.copy(alpha = 0.1f), modifier = Modifier.padding(top = 12.dp))
        }
    }
}


@Composable
fun ProfilePlanSection(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2d4a35).copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Plano Atual",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                text = user.plan ?: "Nenhum plano",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF4CAF50)
            )
        }
    }
}
