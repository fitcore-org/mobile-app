package com.example.fitcore.application.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.fitcore.domain.model.User

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Início", Icons.Default.Home)
    object Workout : Screen("workout", "Treino", Icons.Default.FitnessCenter)
    object Card : Screen("card", "Planos", Icons.Default.Payments)
    object Profile : Screen("profile", "Perfil", Icons.Default.Person)
}

val items = listOf(Screen.Home, Screen.Workout, Screen.Card, Screen.Profile)

@Composable
fun MainScreen(user: User) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            GlassBottomBar(navController = navController)
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
fun GlassBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(24.dp))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(24.dp)
            )
            .shadow(
                elevation = 20.dp,
                ambientColor = Color.Black.copy(alpha = 0.3f),
                spotColor = Color.Black.copy(alpha = 0.3f),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        // Fundo com blur
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.05f))
                .blur(20.dp)
        )

        // Barra de navegação sem blur
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            modifier = Modifier.height(64.dp)
        ) {
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
                    },
                    alwaysShowLabel = false
                )
            }
        }
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
            HomeScreen(
                user = user,
                onNavigateToWorkoutExecution = {
                    navController.navigate("workout_execution")
                },
                onNavigateToExerciseLibrary = {
                    navController.navigate("exercise_library")
                }
            )
        }

        composable(Screen.Workout.route) {
            WorkoutMainScreen(user = user)
        }

        composable(Screen.Card.route) {
            CardScreen(navController = navController) 
        }
        composable("add_card") {
            AddCardScreen(navController = navController)
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