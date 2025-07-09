package com.example.fitcore.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class DashboardItem(val title: String, val icon: ImageVector, val route: String)

val dashboardItems = listOf(
    DashboardItem("Meu Progresso", Icons.Default.TrendingUp, "progress"),
    DashboardItem("Reservar Aulas", Icons.Default.Event, "classes"),
    DashboardItem("Pagamentos", Icons.Default.CreditCard, "payments"),
    DashboardItem("Check-in QR Code", Icons.Default.QrCodeScanner, "qrcode"),
    DashboardItem("Ranking", Icons.Default.EmojiEvents, "ranking"),
    DashboardItem("Falar com Professor", Icons.Default.Chat, "chat")
)

@Composable
fun HomeScreenDashboard() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Bem-vindo, Aluno!", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 24.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(dashboardItems) { item ->
                DashboardCard(item = item)
            }
        }
    }
}

@Composable
fun DashboardCard(item: DashboardItem) {
    Card(
        modifier = Modifier.aspectRatio(1f),
        onClick = { /* TODO: Navegar para a rota do item */ },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(item.icon, contentDescription = item.title, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(item.title, style = MaterialTheme.typography.labelMedium, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}