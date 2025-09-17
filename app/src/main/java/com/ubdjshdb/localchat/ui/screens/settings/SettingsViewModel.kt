package com.ubdjshdb.localchat.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubdjshdb.localchat.core.prefs.AppMode
import com.ubdjshdb.localchat.core.prefs.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appPreferences: AppPreferences
) : ViewModel() {

    val username: StateFlow<String> = appPreferences.username
        .map { it!! }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val appMode: StateFlow<AppMode> = appPreferences.appMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppMode.Client)

    val serverIp: StateFlow<String> = appPreferences.serverIp
        .map { it!! }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    fun setUsername(username: String) {
        viewModelScope.launch {
            appPreferences.setUsername(username)
        }
    }

    fun setAppMode(appMode: AppMode) {
        viewModelScope.launch {
            appPreferences.setAppMode(appMode)
        }
    }

    fun setServerIp(ip: String) {
        viewModelScope.launch {
            appPreferences.setServerIp(ip)
        }
    }
}
