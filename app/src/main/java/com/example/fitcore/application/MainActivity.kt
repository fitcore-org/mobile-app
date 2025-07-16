package com.example.fitcore.application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fitcore.application.di.ViewModelFactoryProvider
import com.example.fitcore.application.ui.login.LoginScreen
import com.example.fitcore.application.ui.main.MainScreen
import com.example.fitcore.application.viewmodel.LoginViewModel
import com.example.fitcore.domain.model.User
import com.example.fitcore.ui.theme.FitcoreTheme
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
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            val loginViewModel: LoginViewModel = viewModel(factory = ViewModelFactoryProvider.provideLoginViewModelFactory())
            LoginScreen(
                loginViewModel = loginViewModel,
                onLoginSuccess = { userJson ->
                    navController.navigate("main/$userJson") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable(
            "main/{userJson}",
            arguments = listOf(navArgument("userJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val userJson = backStackEntry.arguments?.getString("userJson")
            val user = Gson().fromJson(userJson, User::class.java)
            MainScreen(user = user)
        }
    }
}