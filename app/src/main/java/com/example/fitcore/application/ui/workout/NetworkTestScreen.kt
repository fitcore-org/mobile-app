package com.example.fitcore.application.ui.workout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun NetworkTestScreen() {
    var testResult by remember { mutableStateOf("Testando...") }
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val urls = listOf(
                    "http://10.0.2.2:8082/api/v1/workouts/public/enriched",
                    "http://localhost:8082/api/v1/workouts/public/enriched",
                    "http://192.168.1.100:8082/api/v1/workouts/public/enriched" // substitua pelo seu IP local
                )
                
                val results = mutableListOf<String>()
                
                urls.forEach { urlString ->
                    try {
                        val url = URL(urlString)
                        val connection = url.openConnection() as HttpURLConnection
                        connection.requestMethod = "GET"
                        connection.connectTimeout = 5000
                        connection.readTimeout = 5000
                        
                        val responseCode = connection.responseCode
                        results.add("$urlString: $responseCode")
                    } catch (e: Exception) {
                        results.add("$urlString: ERRO - ${e.message}")
                    }
                }
                
                testResult = results.joinToString("\n")
            } catch (e: Exception) {
                testResult = "Erro geral: ${e.message}"
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Teste de Conectividade",
            style = MaterialTheme.typography.headlineSmall
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = testResult,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
