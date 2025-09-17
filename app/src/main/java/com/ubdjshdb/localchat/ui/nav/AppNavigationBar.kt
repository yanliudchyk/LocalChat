package com.ubdjshdb.localchat.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppNavigationBar(navController: NavHostController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    NavigationBar {
        NavigationBarItem(
            selected = currentBackStackEntry?.destination?.route == Routes.CHAT,
            onClick = { navController.navigate(Routes.CHAT) },
            label = { Text("Chat") },
            icon = { Icon(Icons.Default.ChatBubble, contentDescription = "Chat") }
        )
        NavigationBarItem(
            selected = currentBackStackEntry?.destination?.route == Routes.SETTINGS,
            onClick = { navController.navigate(Routes.SETTINGS) },
            label = { Text("Settings") },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") }
        )
    }
}