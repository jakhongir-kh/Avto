package com.uztest.avto.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.uztest.avto.presentation.screen.aichat.AIChatScreen
import com.uztest.avto.presentation.screen.profile.ProfileScreen
import com.uztest.avto.presentation.screen.test.TestScreen
import com.uztest.avto.presentation.screen.tests.TestsScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Tests.route
    ) {
        composable(BottomNavItem.Tests.route) {
            TestsScreen(
                onCategoryClick = { categoryId ->
                    navController.navigate("test/$categoryId")
                }
            )
        }
        
        composable(BottomNavItem.AIChat.route) {
            AIChatScreen()
        }
        
        composable(BottomNavItem.Profile.route) {
            ProfileScreen()
        }
        
        composable("test/{categoryId}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull() ?: 1
            TestScreen(
                categoryId = categoryId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}