package com.ubdjshdb.localchat.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubdjshdb.localchat.core.prefs.AppMode
import com.ubdjshdb.localchat.core.prefs.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(settingsRepository.config.value)
    val uiState = _uiState.asStateFlow()

    fun setUsername(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun setAppMode(appMode: AppMode) {
        _uiState.update { it.copy(appMode = appMode) }
    }

    fun setServerIp(serverIp: String) {
        _uiState.update { it.copy(serverIp = serverIp) }
    }

    fun setServerPort(serverPort: String) {
        _uiState.update { it.copy(serverPort = serverPort.toIntOrNull() ?: 0) }
    }

    fun saveSettings() {
        settingsRepository.updateConfig(_uiState.value)
    }
}
