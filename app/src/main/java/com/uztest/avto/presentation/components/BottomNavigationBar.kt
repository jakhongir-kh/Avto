package com.uztest.avto.presentation.components

import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.uztest.avto.presentation.navigation.BottomNavItem

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("tests", "Tests", Icons.Outlined.Quiz),
        BottomNavItem("ai_chat", "AI Chat", Icons.Outlined.Chat),
        BottomNavItem("profile", "Profile", Icons.Outlined.Person)
    )
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    NavigationBar(
        containerColor = androidx.compose.ui.graphics.Color.White,
        contentColor = androidx.compose.ui.graphics.Color(0xFF90CAF9)
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { 
                    Icon(
                        item.icon, 
                        contentDescription = item.title,
                        tint = if (currentRoute == item.route) 
                            androidx.compose.ui.graphics.Color(0xFF90CAF9)
                        else 
                            androidx.compose.ui.graphics.Color(0xFF9E9E9E)
                    ) 
                },
                label = { 
                    Text(
                        item.title,
                        color = if (currentRoute == item.route) 
                            androidx.compose.ui.graphics.Color(0xFF90CAF9)
                        else 
                            androidx.compose.ui.graphics.Color(0xFF9E9E9E),
                        style = MaterialTheme.typography.labelSmall
                    ) 
                },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = androidx.compose.ui.graphics.Color(0xFF90CAF9),
                    unselectedIconColor = androidx.compose.ui.graphics.Color(0xFF9E9E9E)
                )
            )
        }
    }
}