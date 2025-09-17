package com.ubdjshdb.localchat.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ubdjshdb.localchat.core.prefs.AppMode

@Composable
fun SettingsDialog(
    vm: SettingsViewModel,
    dismiss: () -> Unit
) {
    val currentUsername by vm.username.collectAsState()
    val appMode by vm.appMode.collectAsState()
    val currentServerIp by vm.serverIp.collectAsState()

    var editedUsername by remember { mutableStateOf(currentUsername) }
    var editedServerIp by remember { mutableStateOf(currentServerIp) }

    LaunchedEffect(currentUsername) {
        if (editedUsername != currentUsername) {
            editedUsername = currentUsername
        }
    }
    LaunchedEffect(currentServerIp) {
        if (editedServerIp != currentServerIp) {
            editedServerIp = currentServerIp
        }
    }

    AlertDialog(
        onDismissRequest = dismiss,
        title = { Text("Settings") },
        text = {
            Column {
                OutlinedTextField(
                    value = editedUsername,
                    onValueChange = { editedUsername = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Role:", style = MaterialTheme.typography.titleSmall)
                Row(Modifier.fillMaxWidth()) {
                    AppMode.entries.forEach { mode ->
                        Row(
                            Modifier
                                .selectable(
                                    selected = (appMode == mode),
                                    onClick = { vm.setAppMode(mode) }
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (appMode == mode),
                                onClick = { vm.setAppMode(mode) }
                            )
                            Text(
                                text = mode.name,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                when (appMode) {
                    AppMode.Client -> {
                        OutlinedTextField(
                            value = editedServerIp,
                            onValueChange = { editedServerIp = it },
                            label = { Text("Server IP Address") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    AppMode.Server -> {
                        Text("Current IP: [Placeholder - Implement IP detection]") // Placeholder
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                vm.setUsername(editedUsername)
                if (appMode == AppMode.Client) {
                    vm.setServerIp(editedServerIp)
                }
                // AppMode is already updated in the ViewModel upon selection
                dismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = dismiss) {
                Text("Cancel")
            }
        }
    )
}
