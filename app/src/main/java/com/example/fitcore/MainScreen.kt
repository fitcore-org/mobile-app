package com.example.fitcore.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fitcore.ViewModelFactory
import com.example.fitcore.WorkoutRepository
import com.example.fitcore.WorkoutViewModel

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Início", Icons.Default.Home)
    object Workout : Screen("workout", "Treino", Icons.Default.FitnessCenter)
    object Classes : Screen("classes", "Aulas", Icons.Default.DateRange)
    object Profile : Screen("profile", "Perfil", Icons.Default.Person)
}

val items = listOf(
    Screen.Home,
    Screen.Workout,
    Screen.Classes,
    Screen.Profile,
)

@Composable
fun MainScreen(userId: Int) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            userId = userId
        )
    }
}

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier, userId: Int) {
    NavHost(navController, startDestination = Screen.Home.route, modifier = modifier) {
        composable(Screen.Home.route) { HomeScreenDashboard() }
        composable(Screen.Workout.route) {
            val workoutViewModel: WorkoutViewModel = viewModel(factory = ViewModelFactory { WorkoutViewModel(WorkoutRepository(), userId) })
            WorkoutScreen(viewModel = workoutViewModel)
        }
        composable(Screen.Classes.route) { ClassesScreen() }
        composable(Screen.Profile.route) { ProfileScreen() }
    }
}