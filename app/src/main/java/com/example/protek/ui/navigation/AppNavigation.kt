package com.example.protek.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.protek.ui.detection.DetectionScreen
import com.example.protek.ui.home.HomeScreen
import com.example.protek.utils.DetectionCategory

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(
            route = Screen.Home.route
        ) {
            HomeScreen(
                onNavigate = { category ->
                    navController.navigate(Screen.Detection.createRoute(category))
                }
            )
        }

        composable(
            route = Screen.Detection.route,
            arguments = listOf(
                navArgument("detectionType") {
                    type = NavType.StringType
                }
            )
        ) {
            val typeString = it.arguments?.getString("detectionType")

            val detectionCategory = try {
                if (typeString != null) {
                    DetectionCategory.valueOf(typeString)
                } else {
                    DetectionCategory.SCAM_MESSAGE
                }
            } catch (e: IllegalArgumentException) {
                DetectionCategory.SCAM_MESSAGE
            }

            DetectionScreen(
                detectionType = detectionCategory
            )
        }
    }
    
}