package com.ubdjshdb.localchat.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.ubdjshdb.localchat.ui.screens.chat.ChatScreen
import com.ubdjshdb.localchat.ui.screens.chat.ChatViewModel
import com.ubdjshdb.localchat.ui.screens.settings.SettingsDialog
import com.ubdjshdb.localchat.ui.screens.settings.SettingsViewModel

object Routes {
    const val CHAT = "chat"
    const val SETTINGS = "settings"
}

fun NavHostController.navigateToChat() = navigate(Routes.CHAT)
fun NavHostController.navigateToSettings() = navigate(Routes.SETTINGS)

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Routes.CHAT,
        modifier = modifier,
    ) {
        composable(Routes.CHAT) {
            val vm = hiltViewModel<ChatViewModel>()
            ChatScreen(vm, navController::navigateToSettings)
        }

        dialog(Routes.SETTINGS) {
            val vm = hiltViewModel<SettingsViewModel>()
            SettingsDialog(vm, dismiss = navController::popBackStack)
        }
    }
}