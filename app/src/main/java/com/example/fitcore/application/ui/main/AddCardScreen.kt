package com.example.fitcore.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(navController: NavController) {
    var cardName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }

    // Determina se os dados são válidos para habilitar o botão
    val isFormValid = cardName.isNotBlank() && cardNumber.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Cartão") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = cardName,
                onValueChange = { cardName = it },
                label = { Text("Nome do cartão") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = cardNumber,
                onValueChange = {
                    // Permite apenas dígitos e limita o tamanho (opcional)
                    if (it.all { char -> char.isDigit() }) {
                         cardNumber = it
                    }
                },
                label = { Text("Número do cartão") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { 
                    // Aqui você deve implementar a lógica para salvar o cartão
                    // Por exemplo, salvar no banco de dados, SharedPreferences, etc.
                    
                    // Exemplo de como poderia ser:
                    // saveCard(cardName, cardNumber)
                    
                    // Depois de salvar, navegar de volta
                    navController.navigateUp()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid
            ) {
                Text("Salvar")
            }
        }
    }
}