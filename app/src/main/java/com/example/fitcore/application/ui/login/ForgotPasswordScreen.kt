package com.example.fitcore.application.ui.login

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onNavigateBackToLogin: () -> Unit,
    onNavigateToVerification: () -> Unit, // <-- 1. PARÂMETRO ADICIONADO
    // onSendCode: (String) -> Unit // Lógica do ViewModel viria aqui
) {
    var email by remember { mutableStateOf("") }

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
                .padding(horizontal = 32.dp),
            contentAlignment = Alignment.Center
        ) {
             // Ícone para voltar
            IconButton(
                onClick = onNavigateBackToLogin,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar para o Login",
                    tint = Color.White
                )
            }

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Spacer(
                    modifier = glassmorphismModifier
                        .fillMaxWidth()
                        .height(450.dp) // Altura ajustada para o conteúdo
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
                        text = "Recuperação de Senha",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Digite seu email para receber o código de verificação",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(48.dp))

                    LoginTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "Email"
                    )
                    Spacer(Modifier.height(32.dp))

                    Button(
                        // 2. AÇÃO DO BOTÃO ATUALIZADA PARA NAVEGAR
                        onClick = { onNavigateToVerification() /* TODO: Chamar ViewModel para enviar o código */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("ENVIAR CÓDIGO", fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(24.dp))

                    ClickableText(
                        text = buildAnnotatedString {
                            append("Lembrou da senha? ")
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("fazer login")
                            }
                        },
                        onClick = { onNavigateBackToLogin() },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}
