package com.example.fitcore.application.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.fitcore.domain.model.User

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Início", Icons.Default.Home)
    object Workout : Screen("workout", "Treino", Icons.Default.FitnessCenter)
    object Classes : Screen("classes", "Aulas", Icons.Default.DateRange)
    object Profile : Screen("profile", "Perfil", Icons.Default.Person)
}

val items = listOf(Screen.Home, Screen.Workout, Screen.Classes, Screen.Profile)

@Composable
fun MainScreen(user: User) {
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
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
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
            user = user
        )
    }
}

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier, user: User) {
    NavHost(
        navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(user = user)
        }

        composable(Screen.Workout.route) {
            WorkoutMainScreen(user = user)
        }

        composable(Screen.Classes.route) {
            ClassesScreen()
        }

        composable(Screen.Profile.route) {
            ProfileScreen(user = user)
        }

        composable("workout_execution") {
            com.example.fitcore.application.ui.workout.WorkoutExecutionScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable("exercise_library") {
            com.example.fitcore.application.ui.exercise.ExerciseLibraryScreen(
                onNavigateBack = { navController.navigateUp() },
                onExerciseClick = { exercise ->
                    navController.navigate("exercise_detail/${exercise.id}")
                }
            )
        }

        composable("exercise_detail/{exerciseId}") { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: ""
            com.example.fitcore.application.ui.exercise.ExerciseDetailScreen(
                exerciseId = exerciseId,
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}