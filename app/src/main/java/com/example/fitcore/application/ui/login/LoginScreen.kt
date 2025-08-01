package com.example.fitcore.application.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fitcore.R
import com.example.fitcore.application.viewmodel.LoginUiState
import com.example.fitcore.application.viewmodel.LoginViewModel
import com.google.gson.Gson

/**
 * The login screen has been redesigned to reflect the Fitcore web application.
 *
 * A subtle vertical gradient sits behind the entire screen, cards use a
 * translucent surface colour and the primary action button uses the new
 * green accent defined in the theme. Input fields inherit colours from
 * the theme and provide gentle feedback when focused. All text uses
 * Montserrat via the shared Typography defined in ui/theme/Type.kt.
 */
@Composable
fun LoginScreen(loginViewModel: LoginViewModel, onLoginSuccess: (String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by loginViewModel.uiState.collectAsState()

    // Navigate after successful login
    LaunchedEffect(key1 = uiState) {
        if (uiState is LoginUiState.Success) {
            val userJson = Gson().toJson((uiState as LoginUiState.Success).user)
            onLoginSuccess(userJson)
        }
    }

    // Gradient background reminiscent of the web login page【610871410387510†L0-L4】.
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.surfaceVariant
        )
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            // Application logo
            Image(
                painter = painterResource(id = R.drawable.fitcore_logo),
                contentDescription = "FitCore Logo",
                modifier = Modifier.size(150.dp)
            )

            // Card containing form fields
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "LOGIN",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(24.dp))
                    LoginTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email"
                    )
                    Spacer(Modifier.height(16.dp))
                    LoginTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Senha",
                        isPassword = true
                    )
                    Spacer(Modifier.height(24.dp))
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
                        Text("Entrar", style = MaterialTheme.typography.labelMedium)
                    }
                    Spacer(Modifier.height(16.dp))
                    ClickableText(
                        text = AnnotatedString("Esqueceu sua senha? redefinir senha"),
                        onClick = { /* TODO: Navegar para tela de redefinição */ },
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
            // Feedback area for loading or error messages
            Box(
                modifier = Modifier.height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                when (val state = uiState) {
                    is LoginUiState.Loading -> CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                    is LoginUiState.Error -> Text(
                        state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary,
            focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    )
}