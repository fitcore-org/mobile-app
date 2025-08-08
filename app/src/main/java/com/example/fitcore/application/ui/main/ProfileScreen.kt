package com.example.fitcore.application.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.fitcore.domain.model.User

@Composable
fun ProfileScreen(user: User) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0a0a0a),
            Color(0xFF1a1a1a),
            Color(0xFF2d4a35),
            Color(0xFF1f3a26),
            Color(0xFF0d0f0d)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ProfileHeader(user = user)
            }
            item {
                ProfileInfoSection(user = user)
            }
            item {
                ProfilePlanSection(user = user)
            }
        }
    }
}

@Composable
fun ProfileHeader(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = user.photoUrl,
                contentDescription = "Foto de Perfil",
                // SOLUÇÃO DEFINITIVA APLICADA AQUI:
                placeholder = rememberVectorPainter(image = Icons.Default.Person),
                error = rememberVectorPainter(image = Icons.Default.Person),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = user.name ?: "Nome não informado",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Text(
                text = user.email ?: "Email não informado",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun ProfileInfoSection(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.05f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Informações Pessoais",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Divider(color = Color.White.copy(alpha = 0.2f), thickness = 1.dp)

            ProfileInfoRow("Telefone", user.phone ?: "Não informado")
            ProfileInfoRow("CPF", user.cpf ?: "Não informado")
            ProfileInfoRow("Data de Nascimento", user.birthDate ?: "Não informado")
            ProfileInfoRow("Altura", if (user.height != null) "${user.height} cm" else "Não informado")
            ProfileInfoRow("Peso", if (user.weight != null) "${user.weight} kg" else "Não informado")
        }
    }
}

@Composable
fun ProfilePlanSection(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2d4a35).copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.7f)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}