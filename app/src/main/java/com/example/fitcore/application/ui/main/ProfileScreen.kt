package com.example.fitcore.application.ui.main
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fitcore.R
import com.example.fitcore.domain.model.User
@Composable
fun ProfileScreen(user: User) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.fitcore_logo), contentDescription = "Foto de Perfil", modifier = Modifier.size(120.dp).clip(CircleShape))
        Spacer(modifier = Modifier.height(16.dp))
        Text(user.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(user.username, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.secondary)
        Spacer(modifier = Modifier.height(32.dp))
        Divider()
        ProfileInfoRow(icon = Icons.Default.Email, label = "Email", value = user.email)
        ProfileInfoRow(icon = Icons.Default.Person, label = "ID de Utilizador", value = user.id.toString())
    }
}
@Composable
fun ProfileInfoRow(icon: ImageVector, label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.secondary)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
    }
}