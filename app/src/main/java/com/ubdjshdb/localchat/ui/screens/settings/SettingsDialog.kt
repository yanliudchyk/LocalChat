package com.ubdjshdb.localchat.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ubdjshdb.localchat.core.prefs.AppMode

@Composable
fun SettingsDialog(
    vm: SettingsViewModel,
    dismiss: () -> Unit
) {
    val uiState by vm.uiState.collectAsState()

    AlertDialog(
        onDismissRequest = dismiss,
        title = { Text("Settings") },
        text = {
            Column {
                OutlinedTextField(
                    value = uiState.username,
                    onValueChange = vm::setUsername,
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
                                    selected = (uiState.appMode == mode),
                                    onClick = { vm.setAppMode(mode) }
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (uiState.appMode == mode),
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
                if (uiState.appMode == AppMode.Client) {
                    OutlinedTextField(
                        value = uiState.serverIp,
                        onValueChange = vm::setServerIp,
                        label = { Text("Server IP Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                OutlinedTextField(
                    value = uiState.serverPort.toString(),
                    onValueChange = vm::setServerPort,
                    label = { Text("Server Port") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                vm.saveSettings()
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
