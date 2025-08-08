package com.example.fitcore.application.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.*

// Cores padronizadas com base na versão web
private val Primary = Color(0xFF22c55e)
private val Secondary = Color(0xFF4ade80)
private val Surface = Color(0xFF1f3a26)
private val OnPrimary = Color.White
private val OnSurface = Color.White

// --- Modelos de Dados e Dados de Exemplo ---
data class CreditCardInfo(
    val brandLogo: Int,
    val lastFourDigits: String,
    val holderName: String,
    val expiryDate: String
)

data class Plan(
    val id: String,
    val name: String,
    val price: String,
    val features: List<String>,
)

val sampleCards = listOf(
    CreditCardInfo(android.R.drawable.ic_menu_camera, "4268", "Seu Nome", "12/27"),
    CreditCardInfo(android.R.drawable.ic_menu_gallery, "5591", "Seu Nome", "08/25")
)

val samplePlans = listOf(
    Plan("basic", "Básico", "R$ 19,90/mês", listOf("Acesso a treinos básicos", "Acompanhamento limitado")),
    Plan("premium", "Premium", "R$ 39,90/mês", listOf("Todos os treinos", "Planos personalizados", "Suporte prioritário"))
)

val currentUserPlanId = "premium"

// --- Navegação ---
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "card_screen") {
        composable("card_screen") { CardScreen(navController) }
        composable("add_card") { AddCardScreen(navController) }
    }
}

// --- Tela de Adicionar Cartão (VERSÃO CORRIGIDA) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(navController: NavController) {
    var cardName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiryMonth by remember { mutableStateOf("") }
    var expiryYear by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    val isFormValid = cardName.isNotBlank() && 
                     cardNumber.length >= 13 && 
                     expiryMonth.isNotBlank() && 
                     expiryYear.isNotBlank() && 
                     cvv.length >= 3

    val backgroundBrush = Brush.linearGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFF0a0a0a),
            1.0f to Color(0xFF1f3a26)
        )
    )

    // Função para formatar o número do cartão
    fun formatCardNumber(number: String): String {
        val cleanNumber = number.replace(" ", "")
        val formatted = StringBuilder()
        for (i in cleanNumber.indices) {
            if (i > 0 && i % 4 == 0) {
                formatted.append(" ")
            }
            formatted.append(cleanNumber[i])
        }
        return formatted.toString()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Adicionar Cartão", 
                        color = OnSurface,
                        style = MaterialTheme.typography.titleLarge
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = OnSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color(0xFF0a0a0a)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundBrush)
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Nome no cartão
            OutlinedTextField(
                value = cardName,
                onValueChange = { cardName = it },
                label = { Text("Nome no cartão") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = OnSurface,
                    unfocusedTextColor = OnSurface,
                    cursorColor = Primary,
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = OnSurface.copy(alpha = 0.5f),
                    focusedLabelColor = Primary,
                    unfocusedLabelColor = OnSurface.copy(alpha = 0.7f)
                )
            )

            // Número do cartão
            OutlinedTextField(
                value = formatCardNumber(cardNumber),
                onValueChange = { newValue ->
                    val cleanValue = newValue.replace(" ", "")
                    if (cleanValue.all { it.isDigit() } && cleanValue.length <= 16) {
                        cardNumber = cleanValue
                    }
                },
                label = { Text("Número do cartão") },
                placeholder = { Text("0000 0000 0000 0000") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = OnSurface,
                    unfocusedTextColor = OnSurface,
                    cursorColor = Primary,
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = OnSurface.copy(alpha = 0.5f),
                    focusedLabelColor = Primary,
                    unfocusedLabelColor = OnSurface.copy(alpha = 0.7f)
                )
            )

            // Data de validade e CVV
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Mês
                OutlinedTextField(
                    value = expiryMonth,
                    onValueChange = { 
                        if (it.all { char -> char.isDigit() } && it.length <= 2) {
                            val month = it.toIntOrNull()
                            if (month == null || month in 1..12) {
                                expiryMonth = it
                            }
                        }
                    },
                    label = { Text("MM") },
                    placeholder = { Text("12") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = OnSurface,
                        unfocusedTextColor = OnSurface,
                        cursorColor = Primary,
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = OnSurface.copy(alpha = 0.5f),
                        focusedLabelColor = Primary,
                        unfocusedLabelColor = OnSurface.copy(alpha = 0.7f)
                    )
                )

                // Ano
                OutlinedTextField(
                    value = expiryYear,
                    onValueChange = { 
                        if (it.all { char -> char.isDigit() } && it.length <= 2) {
                            expiryYear = it
                        }
                    },
                    label = { Text("AA") },
                    placeholder = { Text("27") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = OnSurface,
                        unfocusedTextColor = OnSurface,
                        cursorColor = Primary,
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = OnSurface.copy(alpha = 0.5f),
                        focusedLabelColor = Primary,
                        unfocusedLabelColor = OnSurface.copy(alpha = 0.7f)
                    )
                )

                // CVV
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { 
                        if (it.all { char -> char.isDigit() } && it.length <= 4) {
                            cvv = it
                        }
                    },
                    label = { Text("CVV") },
                    placeholder = { Text("123") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = OnSurface,
                        unfocusedTextColor = OnSurface,
                        cursorColor = Primary,
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = OnSurface.copy(alpha = 0.5f),
                        focusedLabelColor = Primary,
                        unfocusedLabelColor = OnSurface.copy(alpha = 0.7f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botão de salvar
            Button(
                onClick = {
                    if (isFormValid) {
                        // Aqui você pode implementar a lógica para salvar o cartão
                        // Por exemplo, salvar no banco de dados local ou enviar para API
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = OnPrimary,
                    disabledContainerColor = Primary.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Salvar Cartão",
                    modifier = Modifier.padding(vertical = 4.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun CardScreen(navController: NavController) {
    val backgroundBrush = Brush.linearGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFF0a0a0a),
            0.25f to Color(0xFF1a1a1a),
            0.50f to Color(0xFF2d4a35),
            0.75f to Color(0xFF1f3a26),
            1.0f to Color(0xFF0d0f0d)
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Pagamento e Planos",
            style = MaterialTheme.typography.headlineMedium,
            color = OnSurface,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        CardsSection(cards = sampleCards, onAddCardClick = {
            navController.navigate("add_card")
        })

        Spacer(modifier = Modifier.height(32.dp))

        PlansSection(plans = samplePlans, currentPlanId = currentUserPlanId)
    }
}

@Composable
fun CardsSection(cards: List<CreditCardInfo>, onAddCardClick: () -> Unit) {
    Text(
        text = "Meus Cartões",
        style = MaterialTheme.typography.titleLarge,
        color = OnSurface,
        fontWeight = FontWeight.SemiBold
    )
    Spacer(modifier = Modifier.height(16.dp))
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            AddCardButton(onClick = onAddCardClick)
        }
        items(cards) { card ->
            CreditCardItem(cardInfo = card)
        }
    }
}

@Composable
fun AddCardButton(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(width = 240.dp, height = 150.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Adicionar Cartão",
                tint = OnSurface,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Adicionar Cartão", color = OnSurface)
        }
    }
}

@Composable
fun CreditCardItem(cardInfo: CreditCardInfo) {
    Card(
        modifier = Modifier.size(width = 240.dp, height = 150.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "VISA", 
                    color = OnPrimary, 
                    fontSize = 22.sp, 
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = "Chip",
                    tint = OnPrimary.copy(alpha = 0.7f),
                    modifier = Modifier.size(32.dp)
                )
            }
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = "**** **** **** ${cardInfo.lastFourDigits}",
                    color = OnPrimary,
                    fontSize = 18.sp,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text(
                        text = cardInfo.holderName.uppercase(),
                        color = OnPrimary.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = cardInfo.expiryDate,
                        color = OnPrimary.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PlansSection(plans: List<Plan>, currentPlanId: String) {
    Text(
        text = "Planos",
        style = MaterialTheme.typography.titleLarge,
        color = OnSurface,
        fontWeight = FontWeight.SemiBold
    )
    Spacer(modifier = Modifier.height(16.dp))
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        plans.forEach { plan ->
            PlanItem(plan = plan, isCurrentUserPlan = plan.id == currentPlanId)
        }
    }
}

@Composable
fun PlanItem(plan: Plan, isCurrentUserPlan: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isCurrentUserPlan) 2.dp else 0.dp,
                color = if (isCurrentUserPlan) Primary else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = plan.name,
                    color = OnSurface,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = plan.price, 
                    color = Secondary, 
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                plan.features.forEach { feature ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = feature,
                            color = OnSurface.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            if (isCurrentUserPlan) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Plano Atual",
                        color = OnPrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Button(
                    onClick = { /* TODO: Lógica para selecionar plano */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = CircleShape,
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Text(text = "Selecionar", color = OnPrimary)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 380)
@Composable
fun CardScreenPreview() {
    CardScreen(navController = rememberNavController())
}

@Preview(showBackground = true, widthDp = 380)
@Composable
fun AddCardScreenPreview() {
    AddCardScreen(navController = rememberNavController())
}