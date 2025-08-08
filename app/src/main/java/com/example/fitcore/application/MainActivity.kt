package com.example.fitcore.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitcore.application.di.ViewModelFactoryProvider
import com.example.fitcore.application.ui.login.LoginScreen
import com.example.fitcore.application.ui.login.ForgotPasswordScreen
import com.example.fitcore.application.ui.login.VerificationCodeScreen
import com.example.fitcore.application.ui.main.MainScreen
import com.example.fitcore.application.ui.splash.SplashScreen
import com.example.fitcore.application.viewmodel.UserStateViewModel
import com.example.fitcore.application.ui.theme.FitcoreTheme
import com.example.fitcore.application.viewmodel.LoginViewModel
import com.example.fitcore.domain.model.User
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitcoreTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    val userStateViewModel: UserStateViewModel = viewModel(
        factory = ViewModelFactoryProvider.provideUserStateViewModelFactory()
    )

    NavHost(navController = navController, startDestination = "splash") {

        // Rota da SplashScreen
        composable("splash") {
            SplashScreen(onNavigateToLogin = {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }

        // Rota de Login
        composable("login") {
            val loginViewModel: com.example.fitcore.application.viewmodel.LoginViewModel = viewModel(
                factory = ViewModelFactoryProvider.provideLoginViewModelFactory()
            )
            LoginScreen(
                loginViewModel = loginViewModel,
                onLoginSuccess = { user ->
                    userStateViewModel.setUser(user)
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToForgotPassword = {
                    navController.navigate("forgot_password")
                }
            )
        }

        // Rota para a tela de "Esqueceu a Senha"
        composable("forgot_password") {
            ForgotPasswordScreen(
                onNavigateBackToLogin = {
                    navController.popBackStack()
                },
                onNavigateToVerification = {
                    navController.navigate("verification_code")
                }
            )
        }

        composable("verification_code") {
            VerificationCodeScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToNewPassword = {
                    // TODO: Navegar para a tela de criação de nova senha
                }
            )
        }
        
        // Rota da tela principal
        composable("main") {
            val user by userStateViewModel.user.collectAsState()
            user?.let { MainScreen(user = it) }
        }

        // A rota para "exercise_library" foi removida daqui pois já é gerenciada dentro da MainScreen.
    }
}