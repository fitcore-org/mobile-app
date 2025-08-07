package com.example.fitcore.application.ui.login

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitcore.application.viewmodel.LoginUiState
import com.example.fitcore.application.viewmodel.LoginViewModel
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onLoginSuccess: (String) -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by loginViewModel.uiState.collectAsState()

    val backgroundGradient = Brush.linearGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFF0A0A0A),
            0.25f to Color(0xFF1A1A1A),
            0.50f to Color(0xFF2D4A35),
            0.75f to Color(0xFF1F3A26),
            1.0f to Color(0xFF0D0F0D)
        ),
        start = Offset(0f, 0f),
        end = Offset.Infinite
    )

    LaunchedEffect(key1 = uiState) {
        if (uiState is LoginUiState.Success) {
            val userJson = Gson().toJson((uiState as LoginUiState.Success).user)
            onLoginSuccess(userJson)
        }
    }

    val glassmorphismModifier = Modifier
        .clip(RoundedCornerShape(24.dp))
        .border(
            width = 1.dp,
            color = Color.White.copy(alpha = 0.1f),
            shape = RoundedCornerShape(24.dp)
        )
        .background(Color.White.copy(alpha = 0.05f))
        .then(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Modifier.graphicsLayer {
                    renderEffect = RenderEffect.createBlurEffect(
                        20f, 20f, Shader.TileMode.DECAL
                    ).asComposeRenderEffect()
                }
            } else {
                Modifier
            }
        )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Spacer(
                        modifier = glassmorphismModifier
                            .fillMaxWidth()
                            .height(480.dp)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "FITCORE",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Entre na sua conta",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Spacer(Modifier.height(32.dp))
                        LoginTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = "Email"
                        )
                        Spacer(Modifier.height(16.dp))
                        LoginTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = "Senha",
                            isPassword = true
                        )
                        Spacer(Modifier.height(16.dp))

                        // --- MODIFICAÇÕES AQUI ---
                        ClickableText(
                            text = AnnotatedString("Esqueceu sua senha?"),
                            onClick = { onNavigateToForgotPassword() },
                            // 1. Alinha este item específico para a esquerda (início)
                            modifier = Modifier.align(Alignment.Start),
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                fontSize = 14.sp,
                                // 2. Adiciona o sublinhado
                                textDecoration = TextDecoration.Underline
                            )
                        )
                        // --- FIM DAS MODIFICAÇÕES ---

                        Spacer(Modifier.height(32.dp))
                        Button(
                            onClick = { loginViewModel.login(email, password) },
                            enabled = uiState !is LoginUiState.Loading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text("ENTRAR", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                if (uiState is LoginUiState.Loading || uiState is LoginUiState.Error) {
                    Box(
                        modifier = Modifier
                            .height(50.dp)
                            .padding(top = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        when (val state = uiState) {
                            is LoginUiState.Loading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            is LoginUiState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error)
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            cursorColor = MaterialTheme.colorScheme.primary,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        )
    )
}
